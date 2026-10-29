package com.prestau.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String raiz() {
        return "redirect:/inicio";
    }

    @GetMapping("/inicio")
    public String inicio() {
        return "inicio";
    }
}
