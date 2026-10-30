package com.altokego.backend.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponseDTO {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String mensaje;
    private final String path;
    private final List<String> detalles;

    public ErrorResponseDTO(int status, String error, String mensaje, String path, List<String> detalles) {
        this.status = status;
        this.error = error;
        this.mensaje = mensaje;
        this.path = path;
        this.detalles = detalles;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMensaje() { return mensaje; }
    public String getPath() { return path; }
    public List<String> getDetalles() { return detalles; }
}
