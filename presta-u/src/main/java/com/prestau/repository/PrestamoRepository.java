package com.prestau.repository;

import com.prestau.domain.Prestamo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {
    boolean existsBySolicitudEquipoIdEquipoAndDevolucionIsNull(Integer equipo);
    List<Prestamo> findBySolicitudUsuarioIdUsuarioOrderByEntregaDesc(Integer usuario);
}
