package com.gft.BTGPactual.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SuscripcionRequest {
    
    @NotNull(message = "El ID del cliente es obligatorio")
    private String clienteId;
    
    @NotNull(message = "El ID del fondo es obligatorio")
    private String fondoId;
    
    @NotNull(message = "El monto a vincular es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    private BigDecimal montoVinculado;
} 