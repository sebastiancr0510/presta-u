package com.prestau.service;

import com.prestau.domain.Usuario;
import com.prestau.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
        var usuarioOpt = usuarios.findById(usuarioDemo);
        if (usuarioOpt.isEmpty() || !usuarioOpt.get().getActivo()) {
            throw new IllegalArgumentException("Configure un usuario de demostración activo.");
        }
        return usuarioOpt.get();
    }
}
