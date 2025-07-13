package com.gft.BTGPactual.service.impl;

import com.gft.BTGPactual.model.Cliente;
import com.gft.BTGPactual.model.Fondo;
import com.gft.BTGPactual.model.Suscripcion;
import com.gft.BTGPactual.model.Transaccion;
import com.gft.BTGPactual.model.Usuario;
import com.gft.BTGPactual.service.IDynamoDbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DynamoDbServiceImpl implements IDynamoDbService {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

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
    public void guardarFondo(Fondo fondo) {
        try {
            DynamoDbTable<Fondo> table = dynamoDbEnhancedClient.table(fondosTableName, TableSchema.fromBean(Fondo.class));
            table.putItem(fondo);
            log.info("Fondo guardado en DynamoDB: {}", fondo.getId());
        } catch (DynamoDbException e) {
            log.error("Error al guardar fondo en DynamoDB: {}", e.getMessage());
            throw new RuntimeException("Error al guardar fondo", e);
        }
    }

    @Override
    public Optional<Fondo> obtenerFondo(String id) {
        try {
            DynamoDbTable<Fondo> table = dynamoDbEnhancedClient.table(fondosTableName, TableSchema.fromBean(Fondo.class));
            Key key = Key.builder().partitionValue(id).build();
            Fondo fondo = table.getItem(key);
            return Optional.ofNullable(fondo);
        } catch (DynamoDbException e) {
            log.error("Error al obtener fondo de DynamoDB: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Fondo> obtenerTodosLosFondos() {
        try {
            DynamoDbTable<Fondo> table = dynamoDbEnhancedClient.table(fondosTableName, TableSchema.fromBean(Fondo.class));
            return table.scan().items().stream().collect(Collectors.toList());
        } catch (DynamoDbException e) {
            log.error("Error al obtener fondos de DynamoDB: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public void guardarCliente(Cliente cliente) {
        try {
            DynamoDbTable<Cliente> table = dynamoDbEnhancedClient.table(clientesTableName, TableSchema.fromBean(Cliente.class));
            table.putItem(cliente);
            log.info("Cliente guardado en DynamoDB: {}", cliente.getId());
        } catch (DynamoDbException e) {
            log.error("Error al guardar cliente en DynamoDB: {}", e.getMessage());
            throw new RuntimeException("Error al guardar cliente", e);
        }
    }

    @Override
    public Optional<Cliente> obtenerCliente(String id) {
        try {
            DynamoDbTable<Cliente> table = dynamoDbEnhancedClient.table(clientesTableName, TableSchema.fromBean(Cliente.class));
            Key key = Key.builder().partitionValue(id).build();
            Cliente cliente = table.getItem(key);
            return Optional.ofNullable(cliente);
        } catch (DynamoDbException e) {
            log.error("Error al obtener cliente de DynamoDB: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Cliente> obtenerTodosLosClientes() {
        try {
            DynamoDbTable<Cliente> table = dynamoDbEnhancedClient.table(clientesTableName, TableSchema.fromBean(Cliente.class));
            return table.scan().items().stream().collect(Collectors.toList());
        } catch (DynamoDbException e) {
            log.error("Error al obtener clientes de DynamoDB: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public void guardarSuscripcion(Suscripcion suscripcion) {
        try {
            DynamoDbTable<Suscripcion> table = dynamoDbEnhancedClient.table(suscripcionesTableName, TableSchema.fromBean(Suscripcion.class));
            table.putItem(suscripcion);
            log.info("Suscripción guardada en DynamoDB: {}", suscripcion.getId());
        } catch (DynamoDbException e) {
            log.error("Error al guardar suscripción en DynamoDB: {}", e.getMessage());
            throw new RuntimeException("Error al guardar suscripción", e);
        }
    }

    @Override
    public Optional<Suscripcion> obtenerSuscripcion(String id) {
        try {
            DynamoDbTable<Suscripcion> table = dynamoDbEnhancedClient.table(suscripcionesTableName, TableSchema.fromBean(Suscripcion.class));
            Key key = Key.builder().partitionValue(id).build();
            Suscripcion suscripcion = table.getItem(key);
            return Optional.ofNullable(suscripcion);
        } catch (DynamoDbException e) {
            log.error("Error al obtener suscripción de DynamoDB: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Suscripcion> obtenerSuscripcionesPorCliente(String clienteId) {
        try {
            DynamoDbTable<Suscripcion> table = dynamoDbEnhancedClient.table(suscripcionesTableName, TableSchema.fromBean(Suscripcion.class));
            return table.scan().items().stream()
                    .filter(s -> clienteId.equals(s.getClienteId()))
                    .collect(Collectors.toList());
        } catch (DynamoDbException e) {
            log.error("Error al obtener suscripciones de DynamoDB: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public boolean existeSuscripcionActiva(String clienteId, String fondoId) {
        try {
            DynamoDbTable<Suscripcion> table = dynamoDbEnhancedClient.table(suscripcionesTableName, TableSchema.fromBean(Suscripcion.class));
            return table.scan().items().stream()
                    .anyMatch(s -> clienteId.equals(s.getClienteId()) && 
                                 fondoId.equals(s.getFondoId()) && 
                                 s.getEstado() == Suscripcion.EstadoSuscripcion.ACTIVA);
        } catch (DynamoDbException e) {
            log.error("Error al verificar suscripción en DynamoDB: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void guardarTransaccion(Transaccion transaccion) {
        try {
            DynamoDbTable<Transaccion> table = dynamoDbEnhancedClient.table(transaccionesTableName, TableSchema.fromBean(Transaccion.class));
            table.putItem(transaccion);
            log.info("Transacción guardada en DynamoDB: {}", transaccion.getIdentificadorTransaccion());
        } catch (DynamoDbException e) {
            log.error("Error al guardar transacción en DynamoDB: {}", e.getMessage());
            throw new RuntimeException("Error al guardar transacción", e);
        }
    }

    @Override
    public List<Transaccion> obtenerTransaccionesPorCliente(String clienteId) {
        try {
            DynamoDbTable<Transaccion> table = dynamoDbEnhancedClient.table(transaccionesTableName, TableSchema.fromBean(Transaccion.class));
            return table.scan().items().stream()
                    .filter(t -> clienteId.equals(t.getClienteId()))
                    .sorted((t1, t2) -> t2.getFechaTransaccion().compareTo(t1.getFechaTransaccion()))
                    .collect(Collectors.toList());
        } catch (DynamoDbException e) {
            log.error("Error al obtener transacciones de DynamoDB: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public boolean existeTransaccion(String identificadorTransaccion) {
        try {
            DynamoDbTable<Transaccion> table = dynamoDbEnhancedClient.table(transaccionesTableName, TableSchema.fromBean(Transaccion.class));
            Key key = Key.builder().partitionValue(identificadorTransaccion).build();
            Transaccion transaccion = table.getItem(key);
            return transaccion != null;
        } catch (DynamoDbException e) {
            log.error("Error al verificar transacción en DynamoDB: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void guardarUsuario(Usuario usuario) {
        try {
            DynamoDbTable<Usuario> table = dynamoDbEnhancedClient.table(usuariosTableName, TableSchema.fromBean(Usuario.class));
            table.putItem(usuario);
            log.info("Usuario guardado en DynamoDB: {}", usuario.getUsername());
        } catch (DynamoDbException e) {
            log.error("Error al guardar usuario en DynamoDB: {}", e.getMessage());
            throw new RuntimeException("Error al guardar usuario", e);
        }
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorUsername(String username) {
        try {
            DynamoDbTable<Usuario> table = dynamoDbEnhancedClient.table(usuariosTableName, TableSchema.fromBean(Usuario.class));
            return table.scan().items().stream()
                    .filter(u -> username.equals(u.getUsername()))
                    .findFirst();
        } catch (DynamoDbException e) {
            log.error("Error al obtener usuario de DynamoDB: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Usuario> obtenerTodosLosUsuarios() {
        try {
            DynamoDbTable<Usuario> table = dynamoDbEnhancedClient.table(usuariosTableName, TableSchema.fromBean(Usuario.class));
            return table.scan().items().stream().collect(Collectors.toList());
        } catch (DynamoDbException e) {
            log.error("Error al obtener usuarios de DynamoDB: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public boolean existeUsuario(String username) {
        try {
            DynamoDbTable<Usuario> table = dynamoDbEnhancedClient.table(usuariosTableName, TableSchema.fromBean(Usuario.class));
            return table.scan().items().stream()
                    .anyMatch(u -> username.equals(u.getUsername()));
        } catch (DynamoDbException e) {
            log.error("Error al verificar usuario en DynamoDB: {}", e.getMessage());
            return false;
        }
    }
} 