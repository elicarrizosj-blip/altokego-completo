package com.altokego.backend.dto;

import java.time.LocalDateTime;

public class ViajeResponseDTO {
    private Long id;
    private String origen;
    private String destino;
    private Double precioArs;
    private String metodoPago;
    private String estado;
    private LocalDateTime fechaCreacion;
    private String nombrePasajero;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
    public Double getPrecioArs() { return precioArs; }
    public void setPrecioArs(Double precioArs) { this.precioArs = precioArs; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getNombrePasajero() { return nombrePasajero; }
    public void setNombrePasajero(String nombrePasajero) { this.nombrePasajero = nombrePasajero; }
}
