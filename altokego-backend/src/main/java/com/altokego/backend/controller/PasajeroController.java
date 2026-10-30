package com.altokego.backend.controller;

import com.altokego.backend.dto.PasajeroRequestDTO;
import com.altokego.backend.dto.PasajeroResponseDTO;
import com.altokego.backend.service.PasajeroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pasajeros")
public class PasajeroController {

    private final PasajeroService pasajeroService;

    public PasajeroController(PasajeroService pasajeroService) {
        this.pasajeroService = pasajeroService;
    }

    @PostMapping
    public ResponseEntity<PasajeroResponseDTO> crearPasajero(@Valid @RequestBody PasajeroRequestDTO request) {
        return new ResponseEntity<>(pasajeroService.crearPasajero(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PasajeroResponseDTO>> listarPasajeros() {
        return ResponseEntity.ok(pasajeroService.obtenerTodosLosPasajeros());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PasajeroResponseDTO> obtenerPasajero(@PathVariable Long id) {
        return ResponseEntity.ok(pasajeroService.obtenerPasajeroPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PasajeroResponseDTO> actualizarPasajero(@PathVariable Long id,
                                                                  @Valid @RequestBody PasajeroRequestDTO request) {
        return ResponseEntity.ok(pasajeroService.actualizarPasajero(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPasajero(@PathVariable Long id) {
        pasajeroService.eliminarPasajero(id);
        return ResponseEntity.noContent().build();
    }
}
