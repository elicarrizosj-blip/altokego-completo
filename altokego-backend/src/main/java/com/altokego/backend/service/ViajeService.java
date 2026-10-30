package com.altokego.backend.service;

import com.altokego.backend.dto.ViajeRequestDTO;
import com.altokego.backend.dto.ViajeResponseDTO;
import com.altokego.backend.entity.Pasajero;
import com.altokego.backend.entity.Viaje;
import com.altokego.backend.exception.RecursoNoEncontradoException;
import com.altokego.backend.mapper.ViajeMapper;
import com.altokego.backend.repository.PasajeroRepository;
import com.altokego.backend.repository.ViajeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// @Transactional mantiene abierta la sesión de Hibernate mientras el mapper lee
// el pasajero (LAZY). Sin esto, con open-in-view=false se produce LazyInitializationException.
@Service
@Transactional
public class ViajeService {

    private final ViajeRepository viajeRepository;
    private final PasajeroRepository pasajeroRepository;
    private final ViajeMapper viajeMapper;

    public ViajeService(ViajeRepository viajeRepository,
                        PasajeroRepository pasajeroRepository,
                        ViajeMapper viajeMapper) {
        this.viajeRepository = viajeRepository;
        this.pasajeroRepository = pasajeroRepository;
        this.viajeMapper = viajeMapper;
    }

    // Crear un viaje nuevo asociándolo a un pasajero
    public ViajeResponseDTO crearViaje(ViajeRequestDTO request) {
        Viaje viaje = new Viaje();
        copiarDatos(request, viaje);
        return viajeMapper.toDTO(viajeRepository.save(viaje));
    }

    // Listar todos los viajes
    @Transactional(readOnly = true)
    public List<ViajeResponseDTO> obtenerTodosLosViajes() {
        return viajeRepository.findAll().stream()
                .map(viajeMapper::toDTO)
                .toList();
    }

    // Obtener un viaje por ID
    @Transactional(readOnly = true)
    public ViajeResponseDTO obtenerViajePorId(Long id) {
        return viajeMapper.toDTO(buscarViaje(id));
    }

    // Obtener viajes de un pasajero específico (consulta personalizada pedida por cátedra)
    @Transactional(readOnly = true)
    public List<ViajeResponseDTO> obtenerViajesPorPasajero(Long pasajeroId) {
        if (!pasajeroRepository.existsById(pasajeroId)) {
            throw new RecursoNoEncontradoException("Pasajero no encontrado con ID: " + pasajeroId);
        }
        return viajeRepository.findByPasajeroId(pasajeroId).stream()
                .map(viajeMapper::toDTO)
                .toList();
    }

    // Actualizar un viaje existente
    public ViajeResponseDTO actualizarViaje(Long id, ViajeRequestDTO request) {
        Viaje viaje = buscarViaje(id);
        copiarDatos(request, viaje);
        return viajeMapper.toDTO(viajeRepository.save(viaje));
    }

    // Cambiar el estado de un viaje (lo hace el operador: pendiente, en curso, finalizado o cancelado)
    public ViajeResponseDTO cambiarEstado(Long id, String estado) {
        Viaje viaje = buscarViaje(id);
        viaje.setEstado(estado);
        return viajeMapper.toDTO(viajeRepository.save(viaje));
    }

    // Eliminar un viaje
    public void eliminarViaje(Long id) {
        viajeRepository.delete(buscarViaje(id));
    }

    private Viaje buscarViaje(Long id) {
        return viajeRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Viaje no encontrado con ID: " + id));
    }

    private void copiarDatos(ViajeRequestDTO request, Viaje viaje) {
        Pasajero pasajero = pasajeroRepository.findById(request.getPasajeroId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Pasajero no encontrado con ID: " + request.getPasajeroId()));

        viaje.setOrigen(request.getOrigen());
        viaje.setDestino(request.getDestino());
        viaje.setPrecioArs(request.getPrecioArs());
        viaje.setMetodoPago(request.getMetodoPago());
        viaje.setPasajero(pasajero);
    }
}
