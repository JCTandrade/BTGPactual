package com.gft.BTGPactual.repository;

import com.gft.BTGPactual.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    
    List<Transaccion> findByClienteIdOrderByFechaTransaccionDesc(Long clienteId);
    
    boolean existsByIdentificadorTransaccion(String identificadorTransaccion);
} 