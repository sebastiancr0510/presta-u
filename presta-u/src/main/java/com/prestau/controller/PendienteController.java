package com.prestau.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PendienteController {

    @GetMapping("/solicitudes")
    public String solicitudes(Model model) {
        return pendiente(model, "menu.solicitudes");
    }

    @GetMapping("/prestamos")
    public String prestamos(Model model) {
        return pendiente(model, "menu.prestamos");
    }

    @GetMapping("/categorias")
    public String categorias(Model model) {
        return pendiente(model, "menu.categorias");
    }

    @GetMapping("/perfil")
    public String perfil(Model model) {
        return pendiente(model, "menu.perfil");
    }

    private String pendiente(Model model, String seccion) {
        model.addAttribute("seccion", seccion);
        return "en-construccion";
    }
}
