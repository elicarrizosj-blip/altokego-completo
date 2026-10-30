package com.altokego.backend.mapper;

import com.altokego.backend.dto.RegistroRequestDTO;
import com.altokego.backend.dto.UsuarioResponseDTO;
import com.altokego.backend.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    // La contraseña llega ya hasheada: el mapper nunca trabaja con la contraseña en texto plano
    public Usuario toEntity(RegistroRequestDTO request, String passwordHasheada, String rol) {
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordHasheada);
        usuario.setRol(rol);
        return usuario;
    }

    // La respuesta no incluye la contraseña ni su hash
    public UsuarioResponseDTO toDTO(Usuario usuario) {
        if (usuario == null) return null;

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol());
    }
}
