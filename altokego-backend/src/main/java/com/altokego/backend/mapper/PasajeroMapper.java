package com.altokego.backend.mapper;

import com.altokego.backend.dto.PasajeroRequestDTO;
import com.altokego.backend.dto.PasajeroResponseDTO;
import com.altokego.backend.entity.Pasajero;
import org.springframework.stereotype.Component;

@Component
public class PasajeroMapper {

    public Pasajero toEntity(PasajeroRequestDTO request) {
        Pasajero pasajero = new Pasajero();
        copiarDatos(request, pasajero);
        return pasajero;
    }

    public void copiarDatos(PasajeroRequestDTO request, Pasajero pasajero) {
        pasajero.setNombre(request.getNombre());
        pasajero.setEmail(request.getEmail());
        pasajero.setTelefono(request.getTelefono());
    }

    public PasajeroResponseDTO toDTO(Pasajero pasajero) {
        if (pasajero == null) return null;

        PasajeroResponseDTO dto = new PasajeroResponseDTO();
        dto.setId(pasajero.getId());
        dto.setNombre(pasajero.getNombre());
        dto.setEmail(pasajero.getEmail());
        dto.setTelefono(pasajero.getTelefono());
        return dto;
    }
}
