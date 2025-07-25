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

    @Value("${aws.dynamodb.usuarios-table}")
    private String usuariosTableName;

    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("Inicializando tablas de DynamoDB...");
            
            crearTablaSiNoExiste(fondosTableName, "id");
            crearTablaSiNoExiste(clientesTableName, "id");
            crearTablaSiNoExiste(suscripcionesTableName, "id");
            crearTablaSiNoExiste(transaccionesTableName, "identificadorTransaccion");
            crearTablaSiNoExiste(usuariosTableName, "id");
            
            log.info("Inicialización de DynamoDB completada");
        } catch (Exception e) {
            log.warn("No se pudo inicializar DynamoDB. Error: {}. La aplicación continuará sin DynamoDB.", e.getMessage());
            log.warn("Para usar DynamoDB, configure las credenciales de AWS en las variables de entorno o en env.properties");
        }
    }

    private void crearTablaSiNoExiste(String tableName, String partitionKey) {
        try {
            DescribeTableRequest describeRequest = DescribeTableRequest.builder()
                    .tableName(tableName)
                    .build();
            
            try {
                dynamoDbClient.describeTable(describeRequest);
                log.info("Tabla {} ya existe", tableName);
                return;
            } catch (ResourceNotFoundException e) {
            }

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
        } catch (Exception e) {
            log.error("Error inesperado al crear tabla {}: {}", tableName, e.getMessage());
        }
    }
} 