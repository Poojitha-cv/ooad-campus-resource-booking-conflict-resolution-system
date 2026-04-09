package com.campus.booking.controller;

import com.campus.booking.model.User;
import com.campus.booking.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        try {
            userService.register(user);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @PostMapping("/login")
public String login(@RequestParam String email,
                    @RequestParam String password,
                    Model model,
                    HttpSession session) {

    try {
        User user = userService.login(email, password);

        session.setAttribute("userId", user.getId());
        session.setAttribute("role", user.getRole());

        // FIXED ROLE CHECK
        if ("ADMIN".equalsIgnoreCase(user.getRole().trim())) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/resource/all";
        }

    } catch (Exception e) {
        model.addAttribute("error", e.getMessage());
        return "login";
    }
}
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}