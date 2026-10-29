package com.prestau.controller;

import com.prestau.domain.SolicitudFormulario;
import com.prestau.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NuevaSolicitudController {
    private final SolicitudService solicitudes;

    public NuevaSolicitudController(SolicitudService solicitudes) {
        this.solicitudes = solicitudes;
    }

    @GetMapping("/solicitudes/nueva")
    public String nueva(@RequestParam(required = false) Integer equipoId, Model model) {
        SolicitudFormulario formulario = new SolicitudFormulario();
        formulario.setEquipoId(equipoId);
        model.addAttribute("formulario", formulario);
        cargarEquipos(model);
        return "solicitudes/nueva";
    }

    @PostMapping("/solicitudes/nueva")
    public String guardar(@Valid @ModelAttribute("formulario") SolicitudFormulario formulario,
            BindingResult resultado, Model model, RedirectAttributes flash) {
        if (!resultado.hasErrors()) {
            try {
                var solicitud = solicitudes.crear(formulario);
                flash.addFlashAttribute("mensaje", "Solicitud registrada con estado pendiente.");
                return "redirect:/solicitudes/" + solicitud.getIdSolicitud();
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        cargarEquipos(model);
        return "solicitudes/nueva";
    }

    private void cargarEquipos(Model model) {
        model.addAttribute("equipos", solicitudes.getEquiposSolicitables());
    }
}
