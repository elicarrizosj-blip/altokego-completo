package com.altokego.backend.repository;

import com.altokego.backend.entity.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Long> {

    List<Viaje> findByPasajeroId(Long pasajeroId);

    boolean existsByPasajeroId(Long pasajeroId);
}
