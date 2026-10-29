package com.prestau.service;

import com.prestau.domain.Categoria;
import com.prestau.domain.Equipo;
import com.prestau.domain.Mantenimiento;
import com.prestau.repository.CategoriaRepository;
import com.prestau.repository.EquipoRepository;
import com.prestau.repository.MantenimientoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EquipoService {

    public static final String EN_SERVICIO = "En servicio";
    public static final String MANTENIMIENTO = "Mantenimiento";

    private final EquipoRepository equipoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MantenimientoRepository mantenimientoRepository;

    @Transactional(readOnly = true)
    public List<Equipo> getTodos() {
        return equipoRepository.findTodosOrdenados();
    }

    @Transactional(readOnly = true)
    public List<Equipo> getActivos() {
        return equipoRepository.findByActivoTrueOrderByNombreAsc();
    }

    @Transactional(readOnly = true)
    public List<Equipo> consultaJPQL(String texto, Integer categoriaId) {
        String busqueda = (texto == null) ? "" : texto.trim();
        if (categoriaId == null) {
            return equipoRepository.consultaJPQL(busqueda);
        }
        return equipoRepository.consultaJPQLPorCategoria(busqueda, categoriaId);
    }

    @Transactional(readOnly = true)
    public Optional<Equipo> getEquipo(Integer idEquipo) {
        return equipoRepository.findById(idEquipo);
    }

    @Transactional(readOnly = true)
    public List<Mantenimiento> getHistorial(Integer idEquipo) {
        return mantenimientoRepository.findByEquipoIdEquipoOrderByFechaInicioDesc(idEquipo);
    }

    @Transactional
    public void guardar(Equipo equipo) {
        Integer idCategoria = (equipo.getCategoria() == null) ? null : equipo.getCategoria().getIdCategoria();
        if (idCategoria == null) {
            throw new IllegalArgumentException("Debe seleccionar una categoria.");
        }
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .filter(Categoria::getActiva)
                .orElseThrow(() -> new IllegalArgumentException("La categoria seleccionada no está disponible."));

        if (equipo.getIdEquipo() == null) {
            if (equipoRepository.existsByCodigo(equipo.getCodigo())) {
                throw new IllegalArgumentException("Ya existe un equipo con ese codigo.");
            }
            
            equipo.setCategoria(categoria);
            equipo.setEstadoOperativo(EN_SERVICIO);
            equipo.setActivo(true);
            equipoRepository.save(equipo);
            
        } else {
            Equipo existente = equipoRepository.findById(equipo.getIdEquipo())
                    .orElseThrow(() -> new IllegalArgumentException("El equipo no existe."));
            existente.setCategoria(categoria);
            existente.setNombre(equipo.getNombre());
            existente.setDescripcion(equipo.getDescripcion());
            existente.setRutaImagen(equipo.getRutaImagen());
            if (!existente.getActivo()) {
                throw new IllegalArgumentException("El equipo está inactivo. Actívelo antes de editarlo.");
            }
        }
    }

    @Transactional
    public void inactivar(Integer idEquipo) {
        Equipo equipo = equipoRepository.findById(idEquipo)
                .orElseThrow(() -> new IllegalArgumentException("El equipo no existe."));
        equipo.setActivo(false);
    }
    
    @Transactional
    public void activar(Integer idEquipo) {
        Equipo equipo = equipoRepository.findById(idEquipo)
                .orElseThrow(() -> new IllegalArgumentException("El equipo no existe."));
        if (!Boolean.TRUE.equals(equipo.getCategoria().getActiva()))
            throw new IllegalArgumentException("La categoría del equipo está inactiva.");
        equipo.setActivo(true);
    }

    @Transactional
    public void cambiarEstado(Integer idEquipo, String nuevoEstado, String motivo, String observacion) {
        Equipo equipo = equipoRepository.findById(idEquipo)
                .orElseThrow(() -> new IllegalArgumentException("El equipo no existe."));

        if (MANTENIMIENTO.equals(nuevoEstado)) {
            if (MANTENIMIENTO.equals(equipo.getEstadoOperativo())) {
                throw new IllegalArgumentException("El equipo ya está en mantenimiento.");
            }
            if (motivo == null || motivo.isBlank()) {
                throw new IllegalArgumentException("Debe indicar el motivo del mantenimiento.");
            }
            
            equipo.setEstadoOperativo(MANTENIMIENTO);
            Mantenimiento mantenimiento = new Mantenimiento();
            mantenimiento.setEquipo(equipo);
            mantenimiento.setMotivo(motivo.trim());
            mantenimiento.setObservacion(observacion);
            mantenimiento.setFechaInicio(LocalDateTime.now());
            mantenimientoRepository.save(mantenimiento);

        } else if (EN_SERVICIO.equals(nuevoEstado)) {
            if (EN_SERVICIO.equals(equipo.getEstadoOperativo())) {
                throw new IllegalArgumentException("El equipo ya esta en servicio.");
            }
            equipo.setEstadoOperativo(EN_SERVICIO);
            mantenimientoRepository.findFirstByEquipoIdEquipoAndFechaFinIsNullOrderByFechaInicioDesc(idEquipo)
                    .ifPresent(m -> m.setFechaFin(LocalDateTime.now()));

        } else {
            throw new IllegalArgumentException("El estado indicado no es valido.");
        }

        if (!equipo.getActivo()) {
            throw new IllegalArgumentException("El equipo está inactivo. Actívelo antes de cambiar su estado.");
        }
    }
}