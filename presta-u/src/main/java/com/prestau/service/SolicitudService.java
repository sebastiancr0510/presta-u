package com.prestau.service;

import com.prestau.domain.*;
import com.prestau.repository.*;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SolicitudService {
    private final SolicitudRepository solicitudes;
    private final EquipoRepository equipos;
    private final PrestamoRepository prestamos;
    private final UsuarioActualService usuarioActual;

    @Transactional
    public Solicitud crear(SolicitudFormulario formulario) {
        LocalDateTime ahora = LocalDateTime.now();
        if (formulario.getInicio() == null || formulario.getFin() == null
                || !formulario.getInicio().isAfter(ahora)
                || !formulario.getFin().isAfter(formulario.getInicio()))
            throw new IllegalArgumentException("El retiro debe ser futuro y la devolución posterior al retiro.");
        if (formulario.getMotivo() == null || formulario.getMotivo().isBlank()
                || formulario.getMotivo().length() > 300)
            throw new IllegalArgumentException("Indique un motivo de 1 a 300 caracteres.");
        Equipo equipo = equipos.buscarParaSolicitud(formulario.getEquipoId())
                .filter(e -> Boolean.TRUE.equals(e.getActivo())
                        && EquipoService.EN_SERVICIO.equals(e.getEstadoOperativo()))
                .orElseThrow(() -> new IllegalArgumentException("El equipo no está disponible para solicitar."));
        Usuario usuario = usuarioActual.getActual();
        if (solicitudes.contarSuperpuestas(equipo.getIdEquipo(), EstadoSolicitud.APROBADA,
                formulario.getInicio(), formulario.getFin()) > 0
                || prestamos.existsBySolicitudEquipoIdEquipoAndDevolucionIsNull(equipo.getIdEquipo()))
            throw new IllegalArgumentException("El equipo tiene una reserva superpuesta o un préstamo sin devolver.");
        if (solicitudes.existsByUsuarioIdUsuarioAndEquipoIdEquipoAndInicioAndFinAndEstado(
                usuario.getIdUsuario(), equipo.getIdEquipo(), formulario.getInicio(), formulario.getFin(),
                EstadoSolicitud.PENDIENTE))
            throw new IllegalArgumentException("Ya tiene una solicitud pendiente para ese equipo y esas fechas.");
        Solicitud solicitud = new Solicitud();
        solicitud.setUsuario(usuario);
        solicitud.setEquipo(equipo);
        solicitud.setInicio(formulario.getInicio());
        solicitud.setFin(formulario.getFin());
        solicitud.setMotivo(formulario.getMotivo().trim());
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        solicitud.setCreada(ahora);
        return solicitudes.save(solicitud);
    }
}
