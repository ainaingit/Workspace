package com.example.workspace.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login() { return "login"; }

    @PostMapping("/login_admin")
    public String loginAdmin(@RequestParam String username, @RequestParam String password) {
        return "login";
    }

    @PostMapping("/loginclient")
    public String loginClient(@RequestParam String numero) {
        return "login";
    }
}
