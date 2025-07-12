package com.gft.BTGPactual.repository;

import com.gft.BTGPactual.model.Fondo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FondoRepository extends JpaRepository<Fondo, Long> {
    
    List<Fondo> findByActivoTrue();
    
    Optional<Fondo> findByNombreAndActivoTrue(String nombre);
    
    boolean existsByNombreAndActivoTrue(String nombre);
} 