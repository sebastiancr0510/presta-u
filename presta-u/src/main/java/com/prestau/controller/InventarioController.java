package com.prestau.controller;

import com.prestau.domain.Categoria;
import com.prestau.domain.Equipo;
import com.prestau.service.CategoriaService;
import com.prestau.service.EquipoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inventario")
public class InventarioController {

    private final EquipoService equipoService;
    private final CategoriaService categoriaService;

    @GetMapping
    public String listado(Model model) {
        model.addAttribute("equipos", equipoService.getTodos());
        return "inventario/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        Equipo equipo = new Equipo();
        equipo.setCategoria(new Categoria());
        model.addAttribute("equipo", equipo);
        model.addAttribute("categorias", categoriaService.getActivas());
        return "inventario/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        var equipo = equipoService.getEquipo(id);
        if (equipo.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El equipo no existe.");
            return "redirect:/inventario";
        }
        if (!equipo.get().getActivo()) {
            redirectAttributes.addFlashAttribute("error", "El equipo está inactivo. Actívelo antes de editarlo.");
            return "redirect:/inventario";
        }
        model.addAttribute("equipo", equipo.get());
        model.addAttribute("categorias", categoriaService.getActivas());
        return "inventario/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("equipo") Equipo equipo,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categorias", categoriaService.getActivas());
            return "inventario/formulario";
        }
        try {
            equipoService.guardar(equipo);
            redirectAttributes.addFlashAttribute("mensaje", "El equipo se guardó correctamente.");
            return "redirect:/inventario";
        } catch (IllegalArgumentException e) {
            model.addAttribute("categorias", categoriaService.getActivas());
            model.addAttribute("error", e.getMessage());
            return "inventario/formulario";
        }
    }

    @PostMapping("/inactivar")
    public String inactivar(@RequestParam Integer idEquipo, RedirectAttributes redirectAttributes) {
        try {
            equipoService.inactivar(idEquipo);
            redirectAttributes.addFlashAttribute("mensaje", "El equipo se inactivó correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/inventario";
    }

    @PostMapping("/activar")
    public String activar(@RequestParam Integer idEquipo, RedirectAttributes redirectAttributes) {
        try {
            equipoService.activar(idEquipo);
            redirectAttributes.addFlashAttribute("mensaje", "El equipo se activó correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/inventario";
    }

    @GetMapping("/estado/{id}")
    public String estado(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        var equipo = equipoService.getEquipo(id);
        if (equipo.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El equipo no existe.");
            return "redirect:/inventario";
        }
        if (!equipo.get().getActivo()) {
            redirectAttributes.addFlashAttribute("error", "El equipo está inactivo. Actívelo antes de cambiar su estado.");
            return "redirect:/inventario";
        }
        model.addAttribute("equipo", equipo.get());
        model.addAttribute("historial", equipoService.getHistorial(id));
        return "inventario/estado";
    }

    @PostMapping("/estado")
    public String cambiarEstado(@RequestParam Integer idEquipo,
                                @RequestParam String estado,
                                @RequestParam(required = false) String motivo,
                                @RequestParam(required = false) String observacion,
                                RedirectAttributes redirectAttributes) {
        try {
            equipoService.cambiarEstado(idEquipo, estado, motivo, observacion);
            redirectAttributes.addFlashAttribute("mensaje", "El estado del equipo se actualizó.");
            return "redirect:/inventario";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/inventario/estado/" + idEquipo;
        }
    }
}