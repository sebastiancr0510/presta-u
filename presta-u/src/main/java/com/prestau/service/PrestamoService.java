package com.prestau.service;

import com.prestau.domain.Prestamo;
import com.prestau.repository.PrestamoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrestamoService {
    private final PrestamoRepository prestamos;
    private final UsuarioActualService usuarioActual;

    public PrestamoService(PrestamoRepository prestamos, UsuarioActualService usuarioActual) {
        this.prestamos = prestamos;
        this.usuarioActual = usuarioActual;
    }

    @Transactional(readOnly = true)
    public List<Prestamo> getPropios() {
        return prestamos.findBySolicitudUsuarioIdUsuarioOrderByEntregaDesc(usuarioActual.getActual().getIdUsuario());
    }
}
