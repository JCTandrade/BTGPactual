package com.gft.BTGPactual.repository;

import com.gft.BTGPactual.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByEmailAndActivoTrue(String email);
    
    boolean existsByEmailAndActivoTrue(String email);
} 