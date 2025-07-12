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
public class Fondo {
    
    private String id;
    private String nombre;
    private BigDecimal montoMinimo;
    private CategoriaFondo categoria;
    private boolean activo = true;
    
    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public enum CategoriaFondo {
        FPV, FIC
    }
} 