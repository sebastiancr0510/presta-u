package com.prestau.domain;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class SolicitudFormulario {
    @NotNull(message = "Seleccione un equipo.")
    private Integer equipoId;
    @NotNull(message = "Indique la fecha de retiro.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime inicio;
    @NotNull(message = "Indique la fecha de devolución.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fin;
    @NotBlank(message = "El motivo es obligatorio.")
    @Size(max = 300, message = "El motivo admite como máximo 300 caracteres.")
    private String motivo;
}
