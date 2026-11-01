package com.prestau.repository;

import com.prestau.domain.Mantenimiento;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Integer> {

    Optional<Mantenimiento> findFirstByEquipoIdEquipoAndFechaFinIsNullOrderByFechaInicioDesc(Integer idEquipo);

    List<Mantenimiento> findByEquipoIdEquipoOrderByFechaInicioDesc(Integer idEquipo);
}