package com.prestau.service;

import com.prestau.domain.Usuario;
import com.prestau.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioActualService {
    private final UsuarioRepository usuarios;
    private final Integer usuarioDemo;

    public UsuarioActualService(UsuarioRepository usuarios,
            @Value("${prestau.usuario-demo-id:1}") Integer usuarioDemo) {
        this.usuarios = usuarios;
        this.usuarioDemo = usuarioDemo;
    }

    // Sustituir por el usuario autenticado cuando se implemente el login.
    public Usuario getActual() {
        return usuarios.findById(usuarioDemo).filter(u -> Boolean.TRUE.equals(u.getActivo()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "Configure un usuario de demostración activo."));
    }
}
