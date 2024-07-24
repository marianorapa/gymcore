package com.mrapaport.gymcore.users;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService service) {
        userService = service;
    }

    @GetMapping("/register-client")
    public String showRegisterClientForm(Model model) {
        return "register_client";
    }

    @PostMapping("/register-client")
    public String registerClient(@RequestParam("username") String username,
                                 @RequestParam("dni") String dni,
                                 Model model) {
        var user = userService.registerClient(username, dni);

        model.addAttribute("message", "Cliente registrado. Pin de acceso: " + user.getPin());
        return "register_client";
    }

}
