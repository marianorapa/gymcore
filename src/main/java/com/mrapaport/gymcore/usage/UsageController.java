package com.mrapaport.gymcore.usage;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsageController {

    private final UsageService usageService;

    public UsageController(UsageService service) {
        this.usageService = service;
    }
    @GetMapping("/gym-access")
    public String showAccessForm(Model model) {
        model.addAttribute("showMessage", false);
        return "gym_access";
    }
    @PostMapping("/check-access")
    public String checkAccess(@RequestParam("userId") String userId, Model model) {
        boolean accessGranted = usageService.determineAccess(userId);
        model.addAttribute("accessStatus", accessGranted);
        model.addAttribute("accessMessage", accessGranted ? "Acceso concedido" : "Acceso denegado");
        model.addAttribute("showMessage", true);
        return "gym_access";
    }

}
