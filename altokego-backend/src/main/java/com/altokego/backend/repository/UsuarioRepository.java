package com.altokego.backend.repository;

import com.altokego.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Método mágico de Spring Data JPA para buscar por email
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}
