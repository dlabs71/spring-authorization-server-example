package ru.dlabs.sas.example.jsso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {

    @GetMapping("/login")
    public String index() {
        return "index";
    }
}
