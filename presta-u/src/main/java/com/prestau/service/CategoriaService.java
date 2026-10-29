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

    @Transactional(readOnly = true)
    public List<Categoria> getActivas() {
        return categoriaRepository.findByActivaTrueOrderByNombreAsc();
    }
}