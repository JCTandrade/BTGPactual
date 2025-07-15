package com.gft.BTGPactual.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.math.BigDecimal;
import java.util.List;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private String id;
    private String username;
    private String password;
    private List<Rol> roles;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }


    public enum Rol {
        ADMIN, USER
    }


}
