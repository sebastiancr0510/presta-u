package com.prestau.controller;

import com.prestau.domain.Equipo;
import com.prestau.service.CategoriaService;
import com.prestau.service.EquipoService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/equipos/{id}")
    public String detalle(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Equipo> equipo = equipoService.getEquipo(id);
        if (equipo.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El equipo no existe.");
            return "redirect:/equipos";
        }
        model.addAttribute("equipo", equipo.get());
        return "equipos/detalle";
    }
}
