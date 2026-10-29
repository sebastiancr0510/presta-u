package com.prestau.service;

import com.prestau.domain.Prestamo;
import com.prestau.repository.PrestamoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrestamoService {
    private final PrestamoRepository prestamos;
    private final UsuarioActualService usuarioActual;
    @Transactional(readOnly = true)
    public List<Prestamo> getPropios() {
        return prestamos.findBySolicitudUsuarioIdUsuarioOrderByEntregaDesc(usuarioActual.getActual().getIdUsuario());
    }
}
