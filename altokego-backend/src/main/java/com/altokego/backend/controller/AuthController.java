package com.altokego.backend.controller;

import com.altokego.backend.dto.RegistroRequestDTO;
import com.altokego.backend.dto.UsuarioResponseDTO;
import com.altokego.backend.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // POST público: 201 si se registra, 400 si los datos son inválidos, 409 si el correo ya está en uso.
    // Los errores los resuelve el GlobalExceptionHandler.
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody RegistroRequestDTO request) {
        return new ResponseEntity<>(usuarioService.registrar(request), HttpStatus.CREATED);
    }

    // GET protegido: indica quién está autenticado y con qué rol. La pantalla web lo usa para
    // comprobar las credenciales de Basic Auth y mostrar el rol del usuario.
    @GetMapping("/me")
    public Map<String, String> quienSoy(Authentication authentication) {
        String rol = authentication.getAuthorities().stream()
                .findFirst()
                .map(autoridad -> autoridad.getAuthority().replace("ROLE_", ""))
                .orElse("USER");
        return Map.of("email", authentication.getName(), "rol", rol);
    }
}
