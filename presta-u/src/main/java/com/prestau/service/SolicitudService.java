package com.prestau.service;

import com.prestau.domain.*;
import com.prestau.repository.*;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SolicitudService {
    private final SolicitudRepository solicitudes;
    private final EquipoRepository equipos;
    private final PrestamoRepository prestamos;
    private final UsuarioActualService usuarioActual;

    public SolicitudService(SolicitudRepository solicitudes, EquipoRepository equipos, PrestamoRepository prestamos, UsuarioActualService usuarioActual) {
        this.solicitudes = solicitudes;
        this.equipos = equipos;
        this.prestamos = prestamos;
        this.usuarioActual = usuarioActual;
    }

    @Transactional(readOnly = true)
    public java.util.List<Equipo> getEquiposSolicitables() {
        return equipos.findByActivoTrueAndEstadoOperativoOrderByNombreAsc(EquipoService.EN_SERVICIO);
    }

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
        var equipoOpt = equipos.findById(formulario.getEquipoId());
        if (equipoOpt.isEmpty()) {
            throw new IllegalArgumentException("El equipo no existe.");
        }
        Equipo equipo = equipoOpt.get();
        if (!equipo.getActivo() || !EquipoService.EN_SERVICIO.equals(equipo.getEstadoOperativo())) {
            throw new IllegalArgumentException("El equipo no está disponible para solicitar.");
        }
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

    @Transactional(readOnly = true)
    public java.util.List<Solicitud> getPropias() {
        return solicitudes.findByUsuarioIdUsuarioOrderByCreadaDesc(usuarioActual.getActual().getIdUsuario());
    }

    @Transactional(readOnly = true)
    public Solicitud getPropia(Integer id) {
        var solicitudOpt = solicitudes.findByIdSolicitudAndUsuarioIdUsuario(
                id, usuarioActual.getActual().getIdUsuario());
        if (solicitudOpt.isEmpty()) {
            throw new IllegalArgumentException("La solicitud no existe o no pertenece al usuario actual.");
        }
        return solicitudOpt.get();
    }

    @Transactional
    public void cancelar(Integer id) {
        Solicitud solicitud = getPropia(id);
        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new IllegalArgumentException("Solo se puede cancelar una solicitud pendiente.");
        }
        solicitud.setEstado(EstadoSolicitud.CANCELADA);
        solicitudes.save(solicitud);
    }
}
