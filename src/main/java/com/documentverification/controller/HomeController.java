package com.documentverification.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    @GetMapping("/upload")
    public String uploadPage() {
        return "upload";
    }
    
    @GetMapping("/verify")
    public String verifyPage() {
        return "verify";
    }
    
    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }
}