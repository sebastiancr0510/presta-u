package com.prestau.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "solicitud")
public class Solicitud {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Integer idSolicitud;
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_equipo")
    private Equipo equipo;
    @Column(nullable = false)
    private LocalDateTime inicio;
    @Column(nullable = false)
    private LocalDateTime fin;
    @Column(nullable = false, length = 300)
    private String motivo;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoSolicitud estado;
    @Column(nullable = false)
    private LocalDateTime creada;
    @Column(length = 300)
    private String observacion;
}
