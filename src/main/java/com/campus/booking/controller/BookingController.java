package com.campus.booking.controller;

import com.campus.booking.model.*;
import com.campus.booking.repository.ResourceRepository;
import com.campus.booking.service.BookingService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ResourceRepository resourceRepository;

    @GetMapping("/form/{id}")
    public String bookingForm(@PathVariable Long id, Model model) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found"));
        model.addAttribute("resource", resource);
        return "booking-form";
    }

    @PostMapping("/create")
    public String createBooking(
            @RequestParam Long resourceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(required = false) String purpose,  // ✅ Accept purpose
            Model model,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/";
        }

        BookingRequest request = new BookingRequest();
        request.setResourceId(resourceId);
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        request.setUserId(userId);
        request.setPurpose(purpose);  // ✅ Pass purpose

        Object result = bookingService.createBooking(request);

        if (result instanceof String) {
            model.addAttribute("message", result);
            return "confirmation";
        } else {
            model.addAttribute("conflict", result);
            return "conflict";
        }
    }

    @GetMapping("/availability/{id}")
    public String showAvailability(@PathVariable Long id, Model model) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found"));
        model.addAttribute("resource", resource);
        model.addAttribute("bookings", bookingService.getBookingsByResource(id));
        return "availability";
    }
}