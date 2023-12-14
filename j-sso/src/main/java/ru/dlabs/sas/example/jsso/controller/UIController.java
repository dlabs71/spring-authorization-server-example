package ru.dlabs.sas.example.jsso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {

    @GetMapping("/login")
    public String login() {
        return "index";
    }

    // добавили новый путь, соответствующий пути для главной страницы.
    @GetMapping("/home")
    public String home() {
        return "index";
    }
}
