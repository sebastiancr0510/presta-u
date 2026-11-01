package com.prestau.repository;

import com.prestau.domain.Categoria;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndIdCategoriaNot(String nombre, Integer id);
    List<Categoria> findAllByOrderByNombreAsc();

    List<Categoria> findByActivaTrueOrderByNombreAsc();
}
