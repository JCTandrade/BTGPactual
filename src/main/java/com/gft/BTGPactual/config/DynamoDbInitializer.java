package com.gft.BTGPactual.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(1)
public class DynamoDbInitializer implements CommandLineRunner {

    private final DynamoDbClient dynamoDbClient;

    @Value("${aws.dynamodb.fondos-table}")
    private String fondosTableName;

    @Value("${aws.dynamodb.clientes-table}")
    private String clientesTableName;

    @Value("${aws.dynamodb.suscripciones-table}")
    private String suscripcionesTableName;

    @Value("${aws.dynamodb.transacciones-table}")
    private String transaccionesTableName;

    @Override
    public void run(String... args) throws Exception {
        log.info("Inicializando tablas de DynamoDB...");
        
        crearTablaSiNoExiste(fondosTableName, "id");
        crearTablaSiNoExiste(clientesTableName, "id");
        crearTablaSiNoExiste(suscripcionesTableName, "id");
        crearTablaSiNoExiste(transaccionesTableName, "identificadorTransaccion");
        
        log.info("Inicialización de DynamoDB completada");
    }

    private void crearTablaSiNoExiste(String tableName, String partitionKey) {
        try {
            // Verificar si la tabla existe
            DescribeTableRequest describeRequest = DescribeTableRequest.builder()
                    .tableName(tableName)
                    .build();
            
            try {
                dynamoDbClient.describeTable(describeRequest);
                log.info("Tabla {} ya existe", tableName);
                return;
            } catch (ResourceNotFoundException e) {
                // La tabla no existe, proceder a crearla
            }

            // Crear la tabla
            CreateTableRequest createRequest = CreateTableRequest.builder()
                    .tableName(tableName)
                    .attributeDefinitions(AttributeDefinition.builder()
                            .attributeName(partitionKey)
                            .attributeType(ScalarAttributeType.S)
                            .build())
                    .keySchema(KeySchemaElement.builder()
                            .attributeName(partitionKey)
                            .keyType(KeyType.HASH)
                            .build())
                    .billingMode(BillingMode.PAY_PER_REQUEST)
                    .build();

            CreateTableResponse response = dynamoDbClient.createTable(createRequest);
            log.info("Tabla {} creada exitosamente", tableName);
            
        } catch (DynamoDbException e) {
            log.error("Error al crear tabla {}: {}", tableName, e.getMessage());
        }
    }
} 