package com.altokego.backend.mapper;

import com.altokego.backend.dto.ViajeResponseDTO;
import com.altokego.backend.entity.Viaje;
import org.springframework.stereotype.Component;

@Component
public class ViajeMapper {

    public ViajeResponseDTO toDTO(Viaje viaje) {
        if (viaje == null) return null;

        ViajeResponseDTO dto = new ViajeResponseDTO();
        dto.setId(viaje.getId());
        dto.setOrigen(viaje.getOrigen());
        dto.setDestino(viaje.getDestino());
        dto.setPrecioArs(viaje.getPrecioArs());
        dto.setMetodoPago(viaje.getMetodoPago());
        dto.setEstado(viaje.getEstado());
        dto.setFechaCreacion(viaje.getFechaCreacion());

        if (viaje.getPasajero() != null) {
            dto.setNombrePasajero(viaje.getPasajero().getNombre());
        }

        return dto;
    }
}
