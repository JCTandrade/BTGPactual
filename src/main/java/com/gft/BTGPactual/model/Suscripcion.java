package com.gft.BTGPactual.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Suscripcion {
    
    private String id;
    private String clienteId;
    private String fondoId;
    private BigDecimal montoVinculado;
    private LocalDateTime fechaSuscripcion;
    private LocalDateTime fechaCancelacion;
    private EstadoSuscripcion estado = EstadoSuscripcion.ACTIVA;
    private String identificadorTransaccion;
    
    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public enum EstadoSuscripcion {
        ACTIVA, CANCELADA
    }
} 