package com.mrapaport.gymcore.payments;

import com.mrapaport.gymcore.payments.model.enums.PaymentStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Controller
public class PaymentController {

    private PaymentService paymentService;

    public PaymentController(PaymentService service) {
        this.paymentService = service;
    }

    @GetMapping("/register-payment")
    public String showRegisterPaymentForm(Model model) {
        return "register_payment";
    }

    @PostMapping("/register-payment")
    public String registerPayment(@RequestParam String userDni, @RequestParam double amount, @RequestParam String expiryDate, RedirectAttributes redirectAttributes) {

        paymentService.registerPayment(userDni, amount, LocalDate.parse(expiryDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        redirectAttributes.addFlashAttribute("message", "Pago registrado exitosamente para el usuario con DNI: " + userDni);
        return "redirect:/register-payment";
    }


    @PostMapping("/update-payment-status")
    public String updatePaymentStatus(@RequestParam UUID paymentId, @RequestParam PaymentStatus status) {
        var payment = paymentService.updatePaymentStatus(paymentId, status);
        return "redirect:/user-info/" + payment.getUser().getId();
    }
}
