package com.gft.BTGPactual.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "fondos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fondo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String nombre;
    
    @Column(name = "monto_minimo", nullable = false, precision = 15, scale = 2)
    private BigDecimal montoMinimo;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaFondo categoria;
    
    @Column(nullable = false)
    private boolean activo = true;
    
    public enum CategoriaFondo {
        FPV, FIC
    }
} 