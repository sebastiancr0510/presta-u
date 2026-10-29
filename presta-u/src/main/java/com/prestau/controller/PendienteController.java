package com.prestau.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PendienteController {

    @GetMapping("/perfil")
    public String perfil(Model model) {
        return pendiente(model, "menu.perfil");
    }

    private String pendiente(Model model, String seccion) {
        model.addAttribute("seccion", seccion);
        return "en-construccion";
    }
}
