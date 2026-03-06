package org.example.controller;

import org.example.model.Role;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           RedirectAttributes ra) {
        if (userService.existsByUsername(username)) {
            ra.addFlashAttribute("errorMessage", "Username already taken.");
            return "redirect:/register";
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(Role.CUSTOMER);
        userService.save(user);
        return "redirect:/login?registered";
    }
}
