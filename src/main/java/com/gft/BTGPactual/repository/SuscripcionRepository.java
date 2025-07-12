package com.gft.BTGPactual.repository;

import com.gft.BTGPactual.model.Suscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {
    
    List<Suscripcion> findByClienteIdAndEstado(Long clienteId, Suscripcion.EstadoSuscripcion estado);
    
    Optional<Suscripcion> findByClienteIdAndFondoIdAndEstado(Long clienteId, Long fondoId, Suscripcion.EstadoSuscripcion estado);
    
    boolean existsByClienteIdAndFondoIdAndEstado(Long clienteId, Long fondoId, Suscripcion.EstadoSuscripcion estado);
    
    List<Suscripcion> findByClienteId(Long clienteId);
} 