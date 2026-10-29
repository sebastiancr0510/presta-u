package com.prestau.controller;

import com.prestau.service.EquipoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CatalogoController {

    private final EquipoService equipoService;

    @GetMapping("/equipos")
    public String listado(Model model) {
        model.addAttribute("equipos", equipoService.getActivos());
        return "equipos/listado";
    }
}
