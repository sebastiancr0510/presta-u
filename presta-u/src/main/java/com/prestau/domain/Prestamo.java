package com.prestau.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "prestamo")
public class Prestamo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestamo")
    private Integer idPrestamo;
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_solicitud", unique = true)
    private Solicitud solicitud;
    @Column(nullable = false)
    private LocalDateTime entrega;
    @Column(nullable = false)
    private LocalDateTime vencimiento;
    private LocalDateTime devolucion;

    public String getEstado() {
        if (devolucion != null) return "Devuelto";
        return vencimiento.isBefore(LocalDateTime.now()) ? "Atrasado" : "Activo";
    }
}
