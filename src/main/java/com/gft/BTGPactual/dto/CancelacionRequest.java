package com.gft.BTGPactual.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CancelacionRequest {
    
    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;
    
    @NotNull(message = "El ID del fondo es obligatorio")
    private Long fondoId;
} 