package com.mrapaport.gymcore.payments;

import com.mrapaport.gymcore.payments.model.Payment;
import com.mrapaport.gymcore.payments.model.enums.PaymentStatus;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
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


    @GetMapping("/payment-info/{id}")
    public String paymentInfo(@PathVariable UUID id, Model model) {
        Payment payment = paymentService.findById(id);
        List<PaymentStatus> paymentStatuses = Arrays.asList(PaymentStatus.values());
        model.addAttribute("payment", payment);
        model.addAttribute("paymentStatuses", paymentStatuses);
        return "payment_info";
    }

    @PostMapping("/update-payment-status")
    public String updatePaymentStatus(@RequestParam UUID paymentId,
            @RequestParam PaymentStatus status, HttpServletResponse response) {
        paymentService.updatePaymentStatus(paymentId, status);
        String userId = paymentService.findUserByPaymentId(paymentId).getId().toString();
        response.setHeader("Refresh", "0; URL=/user-info/" + userId);
        return "redirect:/user-info/" + userId;
    }

    @PostMapping("/create-payment/{userId}")
    public String createPayment(@PathVariable UUID userId, @RequestParam double amount, @RequestParam(name = "payment_method") String paymentMethod, RedirectAttributes redirectAttributes) {
        paymentService.createPayment(userId, amount, paymentMethod);

        redirectAttributes.addFlashAttribute("successMessage", "Pago por $" + amount + " registrado exitosamente");

        return "redirect:/user-info/" + userId;
    }

     @GetMapping("/list-payments")
    public String listPayments(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Payment> paymentPage = paymentService.findPaymentsByDateRange(startDate, endDate, pageable);
        
        model.addAttribute("payments", paymentPage.getContent());
        model.addAttribute("page", paymentPage);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        
        return "list_payments";
    }
}
