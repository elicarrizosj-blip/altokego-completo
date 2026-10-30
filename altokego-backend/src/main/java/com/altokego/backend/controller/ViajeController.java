package com.altokego.backend.controller;

import com.altokego.backend.dto.EstadoRequestDTO;
import com.altokego.backend.dto.ViajeRequestDTO;
import com.altokego.backend.dto.ViajeResponseDTO;
import com.altokego.backend.service.ViajeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/viajes")
public class ViajeController {

    private final ViajeService viajeService;

    public ViajeController(ViajeService viajeService) {
        this.viajeService = viajeService;
    }

    // POST: crear un viaje
    @PostMapping
    public ResponseEntity<ViajeResponseDTO> crearViaje(@Valid @RequestBody ViajeRequestDTO request) {
        return new ResponseEntity<>(viajeService.crearViaje(request), HttpStatus.CREATED);
    }

    // GET: listar todos los viajes
    @GetMapping
    public ResponseEntity<List<ViajeResponseDTO>> listarViajes() {
        return ResponseEntity.ok(viajeService.obtenerTodosLosViajes());
    }

    // GET: obtener un viaje por ID
    @GetMapping("/{id}")
    public ResponseEntity<ViajeResponseDTO> obtenerViaje(@PathVariable Long id) {
        return ResponseEntity.ok(viajeService.obtenerViajePorId(id));
    }

    // GET: listar viajes por ID de pasajero (relación JPA)
    @GetMapping("/pasajero/{pasajeroId}")
    public ResponseEntity<List<ViajeResponseDTO>> obtenerViajesPorPasajero(@PathVariable Long pasajeroId) {
        return ResponseEntity.ok(viajeService.obtenerViajesPorPasajero(pasajeroId));
    }

    // PUT: actualizar un viaje
    @PutMapping("/{id}")
    public ResponseEntity<ViajeResponseDTO> actualizarViaje(@PathVariable Long id,
                                                            @Valid @RequestBody ViajeRequestDTO request) {
        return ResponseEntity.ok(viajeService.actualizarViaje(id, request));
    }

    // PATCH: cambiar el estado de un viaje (solo ADMIN, ver SecurityConfig)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ViajeResponseDTO> cambiarEstado(@PathVariable Long id,
                                                          @Valid @RequestBody EstadoRequestDTO request) {
        return ResponseEntity.ok(viajeService.cambiarEstado(id, request.getEstado()));
    }

    // DELETE: eliminar un viaje
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarViaje(@PathVariable Long id) {
        viajeService.eliminarViaje(id);
        return ResponseEntity.noContent().build();
    }
}
