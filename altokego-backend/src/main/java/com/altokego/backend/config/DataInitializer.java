package com.altokego.backend.config;

import com.altokego.backend.entity.Pasajero;
import com.altokego.backend.entity.Usuario;
import com.altokego.backend.entity.Viaje;
import com.altokego.backend.repository.PasajeroRepository;
import com.altokego.backend.repository.UsuarioRepository;
import com.altokego.backend.repository.ViajeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Carga datos de demostración al iniciar la aplicación, para no tener que completar
 * formularios antes de la presentación. Es seguro ejecutarlo varias veces: no duplica nada.
 * Se desactiva con altokego.seed.enabled=false en application.properties.
 * Las credenciales son ficticias y solo sirven para la demo local.
 */
@Configuration
public class DataInitializer {

    @Bean
    @ConditionalOnProperty(name = "altokego.seed.enabled", havingValue = "true", matchIfMissing = true)
    public CommandLineRunner cargarDatosDeDemo(UsuarioRepository usuarioRepository,
                                               PasajeroRepository pasajeroRepository,
                                               ViajeRepository viajeRepository,
                                               PasswordEncoder passwordEncoder) {
        return args -> {
            // Usuarios con las contraseñas guardadas como hash BCrypt
            crearUsuario(usuarioRepository, passwordEncoder,
                    "Admin AltokeGo", "admin@altokego.test", "admin123", "ADMIN");
            crearUsuario(usuarioRepository, passwordEncoder,
                    "Cliente Demo", "cliente@altokego.test", "cliente123", "USER");

            // Pasajeros y viajes de ejemplo, solo si todavía no hay pasajeros cargados
            if (pasajeroRepository.count() > 0) {
                return;
            }

            // El correo del pasajero coincide con el de la cuenta de demo: así la pantalla lo reconoce
            Pasajero cliente = crearPasajero(pasajeroRepository, "Cliente Demo", "cliente@altokego.test", "2644000111");
            Pasajero ana = crearPasajero(pasajeroRepository, "Ana Pérez", "ana.perez@mail.com", "2644123456");
            Pasajero luis = crearPasajero(pasajeroRepository, "Luis Gómez", "luis.gomez@mail.com", "2644765432");
            Pasajero marta = crearPasajero(pasajeroRepository, "Marta Ruiz", "marta.ruiz@mail.com", "2645111222");

            crearViaje(viajeRepository, cliente, "Media Agua", "San Juan", 3150.0, "efectivo", "finalizado");
            crearViaje(viajeRepository, cliente, "San Juan", "Media Agua", 3150.0, "tarjeta", "pendiente");
            crearViaje(viajeRepository, ana, "Media Agua", "Pocito", 2500.0, "transferencia", "en curso");
            crearViaje(viajeRepository, luis, "San Juan", "Caucete", 1850.0, "efectivo", "finalizado");
            crearViaje(viajeRepository, marta, "Media Agua", "Caucete", 3800.0, "efectivo", "pendiente");
        };
    }

    private void crearUsuario(UsuarioRepository repository, PasswordEncoder encoder,
                              String nombre, String email, String password, String rol) {
        if (repository.existsByEmail(email)) {
            return;
        }
        repository.save(new Usuario(nombre, email, encoder.encode(password), rol));
    }

    private Pasajero crearPasajero(PasajeroRepository repository, String nombre, String email, String telefono) {
        Pasajero pasajero = new Pasajero();
        pasajero.setNombre(nombre);
        pasajero.setEmail(email);
        pasajero.setTelefono(telefono);
        return repository.save(pasajero);
    }

    private void crearViaje(ViajeRepository repository, Pasajero pasajero, String origen, String destino,
                            Double precioArs, String metodoPago, String estado) {
        Viaje viaje = new Viaje();
        viaje.setOrigen(origen);
        viaje.setDestino(destino);
        viaje.setPrecioArs(precioArs);
        viaje.setMetodoPago(metodoPago);
        viaje.setEstado(estado);
        viaje.setPasajero(pasajero);
        repository.save(viaje);
    }
}
