package com.altokego.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactivar CSRF para APIs REST (estándar para apis sin formularios web)
                .authorizeHttpRequests(auth -> auth
                        // 1. Rutas PÚBLICAS: la pantalla web, el registro y el manejador de errores de Spring Boot.
                        //    La pantalla solo es HTML: todos sus datos salen de la API, que sigue protegida.
                        .requestMatchers(HttpMethod.GET, "/", "/index.html", "/logo.svg", "/logo.jpg", "/favicon.ico").permitAll()
                        .requestMatchers("/api/auth/register", "/error").permitAll()

                        // 2. Rutas PROTEGIDAS: Crear o modificar viajes requiere estar autenticado
                        .requestMatchers(HttpMethod.POST, "/api/viajes/**").authenticated()

                        // 3. Rutas con ROLES: Solo un usuario con rol ADMIN puede borrar viajes
                        .requestMatchers(HttpMethod.DELETE, "/api/viajes/**").hasRole("ADMIN")

                        // El operador (ADMIN) es quien cambia el estado de los viajes
                        .requestMatchers(HttpMethod.PATCH, "/api/viajes/**").hasRole("ADMIN")

                        // Cualquier otra petición del sistema requiere autenticación obligatoria
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()) // Activamos Basic Auth pedido por la cátedra
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Sin sesiones ( Stateless )

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // El algoritmo BCrypt que exige la consigna
    }
}
