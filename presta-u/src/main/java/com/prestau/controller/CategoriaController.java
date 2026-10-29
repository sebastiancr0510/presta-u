package com.prestau.controller;

import com.prestau.service.CategoriaService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {
    private final CategoriaService categorias;

    public CategoriaController(CategoriaService categorias) {
        this.categorias = categorias;
    }

    @GetMapping
    public String listado(Model model) {
        model.addAttribute("categorias", categorias.getTodas());
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
