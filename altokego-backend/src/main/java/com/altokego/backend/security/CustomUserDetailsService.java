package com.altokego.backend.security;

import com.altokego.backend.entity.Usuario;
import com.altokego.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Buscamos el usuario en nuestra base de datos por su email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        // Le devolvemos los datos a Spring Security para que valide la contraseña y el rol
        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword()) // Contraseña ya hasheada con BCrypt
                .roles(usuario.getRol())         // Asignamos el rol (USER o ADMIN)
                .build();
    }
}
