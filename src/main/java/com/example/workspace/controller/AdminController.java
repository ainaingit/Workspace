package com.example.workspace.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/importerdonnees")
    public String importerdonnees(){
        return "importerdonnées";
    }
}
