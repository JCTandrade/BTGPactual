package com.gft.BTGPactual.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.math.BigDecimal;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    
    private String id;
    private String nombre;
    private String email;
    private String telefono;
    private BigDecimal saldo = new BigDecimal("500000.00");
    private TipoNotificacion tipoNotificacion = TipoNotificacion.EMAIL;
    private boolean activo = true;
    
    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    
    public void setId(String id) {
        this.id = id;
    }
    
    public enum TipoNotificacion {
        EMAIL, SMS, AMBOS
    }
} 