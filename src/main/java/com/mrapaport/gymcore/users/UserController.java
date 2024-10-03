package com.mrapaport.gymcore.users;

import com.mrapaport.gymcore.payments.PaymentPlanService;
import com.mrapaport.gymcore.payments.PaymentService;
import com.mrapaport.gymcore.payments.model.Payment;
import com.mrapaport.gymcore.payments.model.PaymentPlan;
import com.mrapaport.gymcore.payments.model.Promotion;
import com.mrapaport.gymcore.payments.model.PromotionAssignment;
import com.mrapaport.gymcore.users.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
        var promos = paymentPlanService.getAllActivePromos();
        model.addAttribute("paymentPlans", paymentPlans);
        model.addAttribute("promotions", promos);
        return "register_client";
    }

    @PostMapping("/register-client")
    public String registerClient(@RequestParam String username, @RequestParam String dni, @RequestParam UUID paymentPlanId,  @RequestParam String phoneNumber, @RequestParam(required =  false) UUID promotionId, Model model) {
        var user = userService.registerClient(username, dni, paymentPlanId, phoneNumber, promotionId);
        model.addAttribute("message", "Cliente registrado. Pin de acceso: " + user.getPin());
        var paymentPlans = paymentPlanService.getAllActivePaymentPlans();
        var promos = paymentPlanService.getAllActivePromos();
        model.addAttribute("paymentPlans", paymentPlans);
        model.addAttribute("promotions", promos);
        return "register_client";
    }

    @GetMapping("/list-clients")
    public String listClients(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(required = false) String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Order.desc("createdAt")));
        Page<User> clientPage = userService.getAllClients(pageable, search);
        model.addAttribute("users", clientPage.getContent());
        model.addAttribute("page", clientPage);
        return "list_clients";
    }

    @GetMapping("/user-info/{id}")
    public String userInfo(@PathVariable UUID id, @RequestParam(defaultValue = "0") int page, Model model) {
        var user = userService.findById(id);
        Optional<Promotion> promo = user.getActivePromotion().map(PromotionAssignment::getPromotion);
        var promoDescription = promo.map(Promotion::getDescription).orElse("-");
        Pageable pageable = PageRequest.of(page, 10, Sort.by("accessUntil").descending());
        Page<Payment> payments = paymentService.findByUserId(user.getId(), pageable);

        model.addAttribute("user", user);
        model.addAttribute("promo", promoDescription);
        model.addAttribute("payments", payments.getContent());
        model.addAttribute("page", payments);

        return "user-info";
    }


    @GetMapping("/edit-client/{id}")
    public String showEditClientForm(@PathVariable UUID id, Model model) {
        User user = userService.findById(id);
        List<PaymentPlan> paymentPlans = paymentPlanService.getAllActivePaymentPlans();
        List<Promotion> promotions = paymentPlanService.getAllActivePromos();
        model.addAttribute("user", user);
        user.getActivePromotion().ifPresent(promo -> {
            model.addAttribute("activePromoId", promo.getId());
            model.addAttribute("promotionEndDate", promo.getEndDate());
        });
        model.addAttribute("paymentPlans", paymentPlans);
        model.addAttribute("promotions", promotions);
        return "edit-client";
    }

    @PostMapping("/update-client/{id}")
    public String updateClient(@PathVariable UUID id, @ModelAttribute User user, @RequestParam String promotionId, 
    @RequestParam(name = "promotionEndDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate promotionEndDate,
    RedirectAttributes redirectAttributes) {
        userService.updateUser(id, user, promotionId, promotionEndDate);
        redirectAttributes.addFlashAttribute("successMessage", "Socio actualizado correctamente");
        return "redirect:/user-info/" + id;
    }

    @GetMapping("/list-clients-no-access")
    public String listClientsNoAccess(
            Model model) {
        List<User> clientPage = userService.findClientsWithoutValidAccess();
        model.addAttribute("clients", clientPage);
        return "list-clients-no-access";
    }

}
