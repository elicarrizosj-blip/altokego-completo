package com.altokego.backend.service;

import com.altokego.backend.dto.RegistroRequestDTO;
import com.altokego.backend.dto.UsuarioResponseDTO;
import com.altokego.backend.entity.Usuario;
import com.altokego.backend.exception.ConflictoException;
import com.altokego.backend.mapper.UsuarioMapper;
import com.altokego.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    // El registro público siempre asigna el rol básico: nadie puede elegirse ADMIN al registrarse
    private static final String ROL_POR_DEFECTO = "USER";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
    }

    public UsuarioResponseDTO registrar(RegistroRequestDTO request) {
        // 1. Comprobar que el correo no esté repetido (además existe una restricción única en la base)
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new ConflictoException("El correo " + request.getEmail() + " ya está en uso");
        }

        // 2. Guardar solamente el hash BCrypt de la contraseña
        String passwordHasheada = passwordEncoder.encode(request.getPassword());

        // 3. Crear el usuario con el rol por defecto y guardarlo
        Usuario usuario = usuarioMapper.toEntity(request, passwordHasheada, ROL_POR_DEFECTO);
        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }
}
