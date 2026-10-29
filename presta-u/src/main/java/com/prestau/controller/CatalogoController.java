package com.prestau.controller;

import com.prestau.service.CategoriaService;
import com.prestau.service.EquipoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CatalogoController {

    private final EquipoService equipoService;
    private final CategoriaService categoriaService;

    @GetMapping("/equipos")
    public String listado(@RequestParam(required = false) String texto,
                          @RequestParam(required = false) Integer categoriaId,
                          Model model) {
        model.addAttribute("equipos", equipoService.consultaJPQL(texto, categoriaId));
        model.addAttribute("categorias", categoriaService.getActivas());
        model.addAttribute("texto", texto);
        model.addAttribute("categoriaId", categoriaId);
        return "equipos/listado";
    }
}