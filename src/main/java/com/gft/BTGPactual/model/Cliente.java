package com.gft.BTGPactual.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String telefono;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo = new BigDecimal("500000.00"); // Saldo inicial COP $500.000
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_notificacion", nullable = false)
    private TipoNotificacion tipoNotificacion = TipoNotificacion.EMAIL;
    
    @Column(nullable = false)
    private boolean activo = true;
    
    public enum TipoNotificacion {
        EMAIL, SMS, AMBOS
    }
} 