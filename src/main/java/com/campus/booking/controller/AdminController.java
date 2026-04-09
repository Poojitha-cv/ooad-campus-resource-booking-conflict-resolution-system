package com.campus.booking.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session) {

        // BASIC SECURITY
        if (session.getAttribute("userId") == null) {
            return "redirect:/";
        }

        if (!"ADMIN".equalsIgnoreCase((String) session.getAttribute("role"))) {
            return "redirect:/";
        }

        return "admin-dashboard";
    }
}