package com.prestau.controller;

import com.prestau.service.CategoriaService;
import com.prestau.repository.EquipoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/categorias")
public class CategoriaController {
    private final CategoriaService categorias;
    private final EquipoRepository equipos;

    @GetMapping
    public String listado(Model model) {
        var lista = categorias.getTodas();
        var cantidades = new java.util.HashMap<Integer, Long>();
        for (var categoria : lista) cantidades.put(categoria.getIdCategoria(),
                equipos.countByCategoriaIdCategoria(categoria.getIdCategoria()));
        model.addAttribute("categorias", lista);
        model.addAttribute("cantidades", cantidades);
        return "categorias/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam(required = false) Integer idCategoria,
            @RequestParam String nombre, RedirectAttributes flash) {
        try {
            categorias.guardar(idCategoria, nombre);
            flash.addFlashAttribute("mensaje", "Categoría guardada.");
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", e.getMessage());
        } catch (DataIntegrityViolationException e) {
            flash.addFlashAttribute("error", "Ya existe una categoría con ese nombre.");
        }
        return "redirect:/categorias";
    }

    @PostMapping("/inactivar")
    public String inactivar(@RequestParam Integer idCategoria, RedirectAttributes flash) {
        try {
            categorias.inactivar(idCategoria);
            flash.addFlashAttribute("mensaje", "Categoría inactivada.");
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/categorias";
    }
}
