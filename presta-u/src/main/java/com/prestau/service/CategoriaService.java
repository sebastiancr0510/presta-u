package com.prestau.service;

import com.prestau.domain.Categoria;
import com.prestau.repository.CategoriaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final com.prestau.repository.EquipoRepository equipos;

    @Transactional(readOnly = true)
    public List<Categoria> getActivas() {
        return categoriaRepository.findByActivaTrueOrderByNombreAsc();
    }
    @Transactional(readOnly = true)
    public List<Categoria> getTodas() {
        return categoriaRepository.findAllByOrderByNombreAsc();
    }

    @Transactional
    public void guardar(Integer id, String nombre) {
        String limpio = nombre == null ? "" : nombre.trim();
        if (limpio.isEmpty() || limpio.length() > 50)
            throw new IllegalArgumentException("El nombre debe contener entre 1 y 50 caracteres.");
        boolean repetido = id == null ? categoriaRepository.existsByNombreIgnoreCase(limpio)
                : categoriaRepository.existsByNombreIgnoreCaseAndIdCategoriaNot(limpio, id);
        if (repetido) throw new IllegalArgumentException("Ya existe una categoría con ese nombre.");
        Categoria categoria = new Categoria();
        if (id != null) {
            var categoriaOpt = categoriaRepository.findById(id);
            if (categoriaOpt.isEmpty()) {
                throw new IllegalArgumentException("La categoría no existe.");
            }
            categoria = categoriaOpt.get();
        }
        categoria.setNombre(limpio);
        if (id == null) categoria.setActiva(true);
        categoriaRepository.save(categoria);
    }

    @Transactional
    public void inactivar(Integer id) {
        var categoriaOpt = categoriaRepository.findById(id);
        if (categoriaOpt.isEmpty()) {
            throw new IllegalArgumentException("La categoría no existe.");
        }
        Categoria categoria = categoriaOpt.get();
        if (equipos.existsByCategoriaIdCategoriaAndActivoTrue(id))
            throw new IllegalArgumentException("No se puede inactivar una categoría con equipos activos.");
        categoria.setActiva(false);
        categoriaRepository.save(categoria);
    }
}
