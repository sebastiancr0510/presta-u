package com.prestau.controller;

import com.prestau.service.PrestamoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PrestamoController {
    private final PrestamoService prestamos;
    @GetMapping("/prestamos")
    public String historial(Model model) {
        model.addAttribute("prestamos", prestamos.getPropios());
        return "prestamos/historial";
    }
}
