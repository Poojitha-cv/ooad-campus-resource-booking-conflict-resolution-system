package com.campus.booking.controller;

import com.campus.booking.service.ResourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @PostMapping("/add")
    public String addResource(@RequestParam String name,
                             @RequestParam String type) {

        resourceService.addResource(name, type);
        return "redirect:/resource/all";
    }

    @GetMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("resources", resourceService.getAllResources());
        return "search";
    }

    @GetMapping("/search")
    public String search(@RequestParam String type, Model model) {
        model.addAttribute("resources", resourceService.searchByType(type));
        return "search";
    }
}