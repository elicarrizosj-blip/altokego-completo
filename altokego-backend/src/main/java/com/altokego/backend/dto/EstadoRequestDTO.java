package com.altokego.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class EstadoRequestDTO {

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "pendiente|en curso|finalizado|cancelado",
             message = "El estado debe ser: pendiente, en curso, finalizado o cancelado")
    private String estado;

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
