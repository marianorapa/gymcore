package com.mrapaport.gymcore.users;

import com.mrapaport.gymcore.payments.PaymentPlanService;
import com.mrapaport.gymcore.payments.PaymentService;
import com.mrapaport.gymcore.payments.model.Payment;
import com.mrapaport.gymcore.users.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class UserController {

    private UserService userService;
    private PaymentPlanService paymentPlanService;
    private PaymentService paymentService;

    public UserController(UserService service, PaymentPlanService paymentPlanService, PaymentService paymentService) {
        userService = service;
        this.paymentPlanService = paymentPlanService;
        this.paymentService = paymentService;
    }

    @GetMapping("/register-client")
    public String showRegisterClientForm(Model model) {
        var paymentPlans = paymentPlanService.getAllActivePaymentPlans();
        model.addAttribute("paymentPlans", paymentPlans);
        return "register_client";
    }

    @PostMapping("/register-client")
    public String registerClient(@RequestParam String username, @RequestParam String dni, @RequestParam UUID paymentPlanId, Model model) {
        var user = userService.registerClient(username, dni, paymentPlanId);
        model.addAttribute("message", "Cliente registrado. Pin de acceso: " + user.getPin());
        var paymentPlans = paymentPlanService.getAllActivePaymentPlans();
        model.addAttribute("paymentPlans", paymentPlans);
        return "register_client";
    }

    @GetMapping("/list-clients")
    public String listClients(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> clientPage = userService.getAllClients(pageable);
        model.addAttribute("users", clientPage.getContent());
        model.addAttribute("page", clientPage);
        return "list_clients";
    }

    @GetMapping("/user-info/{id}")
    public String userInfo(@PathVariable UUID id, @RequestParam(defaultValue = "0") int page, Model model) {
        User user = userService.findById(id);
        Pageable pageable = PageRequest.of(page, 10, Sort.by("dueDate").ascending());
        Page<Payment> payments = paymentService.findByUserId(user.getId(), pageable);

        model.addAttribute("user", user);
        model.addAttribute("payments", payments.getContent());
        model.addAttribute("page", payments);

        return "user-info";
    }



}
