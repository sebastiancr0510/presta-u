package com.prestau.repository;

import com.prestau.domain.Solicitud;
import com.prestau.domain.EstadoSolicitud;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {
    List<Solicitud> findByUsuarioIdUsuarioOrderByCreadaDesc(Integer usuario);
    Optional<Solicitud> findByIdSolicitudAndUsuarioIdUsuario(Integer id, Integer usuario);

    @Query("SELECT COUNT(s) FROM Solicitud s WHERE s.equipo.idEquipo = :equipo AND s.estado = :estado AND s.inicio < :fin AND s.fin > :inicio")
    long contarSuperpuestas(@Param("equipo") Integer equipo, @Param("estado") EstadoSolicitud estado,
            @Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    boolean existsByUsuarioIdUsuarioAndEquipoIdEquipoAndInicioAndFinAndEstado(
            Integer usuario, Integer equipo, LocalDateTime inicio, LocalDateTime fin, EstadoSolicitud estado);

}
