package com.gft.BTGPactual.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "suscripciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Suscripcion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fondo_id", nullable = false)
    private Fondo fondo;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montoVinculado;
    
    @Column(nullable = false)
    private LocalDateTime fechaSuscripcion;
    
    @Column
    private LocalDateTime fechaCancelacion;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSuscripcion estado = EstadoSuscripcion.ACTIVA;
    
    @Column(nullable = false, unique = true)
    private String identificadorTransaccion;
    
    public enum EstadoSuscripcion {
        ACTIVA, CANCELADA
    }
} 