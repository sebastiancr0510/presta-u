package com.prestau.controller;

import com.prestau.service.SolicitudService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/solicitudes")
public class SolicitudController {
    private final SolicitudService solicitudes;

    public SolicitudController(SolicitudService solicitudes) {
        this.solicitudes = solicitudes;
    }

    @GetMapping
    public String listado(Model model) {
        model.addAttribute("solicitudes", solicitudes.getPropias());
        return "solicitudes/listado";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Integer id, Model model,
            org.springframework.web.servlet.mvc.support.RedirectAttributes flash) {
        try {
            model.addAttribute("solicitud", solicitudes.getPropia(id));
            return "solicitudes/detalle";
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", e.getMessage());
            return "redirect:/solicitudes";
        }
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Integer id,
            org.springframework.web.servlet.mvc.support.RedirectAttributes flash) {
        try {
            solicitudes.cancelar(id);
            flash.addFlashAttribute("mensaje", "Solicitud cancelada.");
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", e.getMessage());
            return "redirect:/solicitudes";
        }
        return "redirect:/solicitudes/" + id;
    }
}
