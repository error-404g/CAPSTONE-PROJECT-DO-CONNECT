package com.doConnect.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String root() {
        return "redirect:/admin/login";
    }

    @GetMapping("/admin")
    public String adminRoot() {
        return "redirect:/admin/login";
    }
}
