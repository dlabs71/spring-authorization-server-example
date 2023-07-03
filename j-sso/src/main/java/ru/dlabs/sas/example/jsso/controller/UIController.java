package ru.dlabs.sas.example.jsso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {

    @GetMapping("/ui-test")
    public String index() {
        return "index";
    }
}
