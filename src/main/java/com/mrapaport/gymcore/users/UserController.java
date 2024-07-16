package com.mrapaport.gymcore.users;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @GetMapping("/register-client")
    public String showRegisterClientForm(Model model) {
        return "register_client";
    }

    @PostMapping("/register-client")
    public String registerClient(@RequestParam("username") String username,
                                 @RequestParam("dni") String dni,
                                 Model model) {
        // Add logic to save the new client
        // For example: clientService.save(new Client(username, dni));

        // Add a success message or redirect to another page
        model.addAttribute("message", "Cliente registrado exitosamente");
        return "redirect:/register-client";
    }

}
