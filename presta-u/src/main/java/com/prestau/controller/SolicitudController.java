package com.prestau.controller;

import com.prestau.service.SolicitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/solicitudes")
public class SolicitudController {
    private final SolicitudService solicitudes;

    @GetMapping
    public String listado(Model model) {
        model.addAttribute("solicitudes", solicitudes.getPropias());
        return "solicitudes/listado";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Integer id, Model model) {
        model.addAttribute("solicitud", solicitudes.getPropia(id));
        return "solicitudes/detalle";
    }
}
