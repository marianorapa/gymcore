package com.mrapaport.gymcore.payments;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
}
