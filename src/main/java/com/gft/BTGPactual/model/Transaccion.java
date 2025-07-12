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
public class Transaccion {
    
    private String identificadorTransaccion;
    private String clienteId;
    private String fondoId;
    private TipoTransaccion tipo;
    private BigDecimal monto;
    private LocalDateTime fechaTransaccion;
    private String descripcion;
    private EstadoTransaccion estado = EstadoTransaccion.EXITOSA;
    
    @DynamoDbPartitionKey
    public String getIdentificadorTransaccion() {
        return identificadorTransaccion;
    }
    
    public void setIdentificadorTransaccion(String identificadorTransaccion) {
        this.identificadorTransaccion = identificadorTransaccion;
    }
    
    public enum TipoTransaccion {
        SUSCRIPCION, CANCELACION
    }
    
    public enum EstadoTransaccion {
        EXITOSA, FALLIDA, PENDIENTE
    }
} 