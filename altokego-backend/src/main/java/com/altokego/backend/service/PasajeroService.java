package com.altokego.backend.service;

import com.altokego.backend.dto.PasajeroRequestDTO;
import com.altokego.backend.dto.PasajeroResponseDTO;
import com.altokego.backend.entity.Pasajero;
import com.altokego.backend.exception.ConflictoException;
import com.altokego.backend.exception.RecursoNoEncontradoException;
import com.altokego.backend.mapper.PasajeroMapper;
import com.altokego.backend.repository.PasajeroRepository;
import com.altokego.backend.repository.ViajeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PasajeroService {

    private final PasajeroRepository pasajeroRepository;
    private final ViajeRepository viajeRepository;
    private final PasajeroMapper pasajeroMapper;

    public PasajeroService(PasajeroRepository pasajeroRepository,
                           ViajeRepository viajeRepository,
                           PasajeroMapper pasajeroMapper) {
        this.pasajeroRepository = pasajeroRepository;
        this.viajeRepository = viajeRepository;
        this.pasajeroMapper = pasajeroMapper;
    }

    public PasajeroResponseDTO crearPasajero(PasajeroRequestDTO request) {
        if (pasajeroRepository.existsByEmail(request.getEmail())) {
            throw new ConflictoException("Ya existe un pasajero con el email: " + request.getEmail());
        }
        Pasajero guardado = pasajeroRepository.save(pasajeroMapper.toEntity(request));
        return pasajeroMapper.toDTO(guardado);
    }

    @Transactional(readOnly = true)
    public List<PasajeroResponseDTO> obtenerTodosLosPasajeros() {
        return pasajeroRepository.findAll().stream()
                .map(pasajeroMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public PasajeroResponseDTO obtenerPasajeroPorId(Long id) {
        return pasajeroMapper.toDTO(buscarPasajero(id));
    }

    public PasajeroResponseDTO actualizarPasajero(Long id, PasajeroRequestDTO request) {
        Pasajero pasajero = buscarPasajero(id);
        if (pasajeroRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new ConflictoException("Ya existe otro pasajero con el email: " + request.getEmail());
        }
        pasajeroMapper.copiarDatos(request, pasajero);
        return pasajeroMapper.toDTO(pasajeroRepository.save(pasajero));
    }

    public void eliminarPasajero(Long id) {
        Pasajero pasajero = buscarPasajero(id);
        if (viajeRepository.existsByPasajeroId(id)) {
            throw new ConflictoException(
                    "No se puede eliminar el pasajero con ID " + id + " porque tiene viajes asociados");
        }
        pasajeroRepository.delete(pasajero);
    }

    private Pasajero buscarPasajero(Long id) {
        return pasajeroRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pasajero no encontrado con ID: " + id));
    }
}
