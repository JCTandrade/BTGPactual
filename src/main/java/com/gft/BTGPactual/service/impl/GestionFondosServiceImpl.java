package com.gft.BTGPactual.service.impl;

import com.gft.BTGPactual.dto.CancelacionRequest;
import com.gft.BTGPactual.dto.SuscripcionRequest;
import com.gft.BTGPactual.dto.TransaccionResponse;
import com.gft.BTGPactual.exception.RecursoNoEncontradoException;
import com.gft.BTGPactual.exception.SaldoInsuficienteException;
import com.gft.BTGPactual.exception.SuscripcionExistenteException;
import com.gft.BTGPactual.model.*;
import com.gft.BTGPactual.service.IDynamoDbService;
import com.gft.BTGPactual.service.IGestionFondosService;
import com.gft.BTGPactual.service.INotificacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GestionFondosServiceImpl implements IGestionFondosService {
    
    private final IDynamoDbService dynamoDbService;
    private final INotificacionService notificacionService;
    
    @Override
    @Transactional
    public TransaccionResponse suscribirseAFondo(SuscripcionRequest request) {
        log.info("Iniciando suscripción: clienteId={}, fondoId={}, monto={}", 
                request.getClienteId(), request.getFondoId(), request.getMontoVinculado());
        Cliente cliente = dynamoDbService.obtenerCliente(request.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));
        Fondo fondo = dynamoDbService.obtenerFondo(request.getFondoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Fondo no encontrado"));
        if (dynamoDbService.existeSuscripcionActiva(request.getClienteId(), request.getFondoId())) {
            throw new SuscripcionExistenteException(fondo.getNombre());
        }
        if (request.getMontoVinculado().compareTo(fondo.getMontoMinimo()) < 0) {
            throw new IllegalArgumentException(
                String.format("El monto mínimo para el fondo %s es COP $%,.2f", 
                    fondo.getNombre(), fondo.getMontoMinimo())
            );
        }
        if (cliente.getSaldo().compareTo(request.getMontoVinculado()) < 0) {
            throw new SaldoInsuficienteException(fondo.getNombre());
        }
        String identificadorTransaccion = generarIdentificadorTransaccion();
        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setId(UUID.randomUUID().toString());
        suscripcion.setClienteId(request.getClienteId());
        suscripcion.setFondoId(request.getFondoId());
        suscripcion.setMontoVinculado(request.getMontoVinculado());
        suscripcion.setFechaSuscripcion(LocalDateTime.now());
        suscripcion.setEstado(Suscripcion.EstadoSuscripcion.ACTIVA);
        suscripcion.setIdentificadorTransaccion(identificadorTransaccion);
        dynamoDbService.guardarSuscripcion(suscripcion);
        cliente.setSaldo(cliente.getSaldo().subtract(request.getMontoVinculado()));
        dynamoDbService.guardarCliente(cliente);
        Transaccion transaccion = new Transaccion();
        transaccion.setIdentificadorTransaccion(identificadorTransaccion);
        transaccion.setClienteId(request.getClienteId());
        transaccion.setFondoId(request.getFondoId());
        transaccion.setTipo(Transaccion.TipoTransaccion.SUSCRIPCION);
        transaccion.setMonto(request.getMontoVinculado());
        transaccion.setFechaTransaccion(LocalDateTime.now());
        transaccion.setDescripcion("Suscripción al fondo " + fondo.getNombre());
        transaccion.setEstado(Transaccion.EstadoTransaccion.EXITOSA);
        dynamoDbService.guardarTransaccion(transaccion);
        notificacionService.enviarNotificacionSuscripcion(cliente, fondo, request.getMontoVinculado());
        log.info("Suscripción exitosa: identificadorTransaccion={}", identificadorTransaccion);
        return TransaccionResponse.fromTransaccion(transaccion);
    }
    
    @Override
    @Transactional
    public TransaccionResponse cancelarSuscripcion(CancelacionRequest request) {
        log.info("Iniciando cancelación: clienteId={}, fondoId={}", 
                request.getClienteId(), request.getFondoId());
        Cliente cliente = dynamoDbService.obtenerCliente(request.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));
        Fondo fondo = dynamoDbService.obtenerFondo(request.getFondoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Fondo no encontrado"));
        List<Suscripcion> suscripciones = dynamoDbService.obtenerSuscripcionesPorCliente(request.getClienteId());
        Suscripcion suscripcion = suscripciones.stream()
                .filter(s -> s.getFondoId().equals(request.getFondoId()) && s.getEstado() == Suscripcion.EstadoSuscripcion.ACTIVA)
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "No se encontró una suscripción activa al fondo " + fondo.getNombre()));
        String identificadorTransaccion = generarIdentificadorTransaccion();
        suscripcion.setEstado(Suscripcion.EstadoSuscripcion.CANCELADA);
        suscripcion.setFechaCancelacion(LocalDateTime.now());
        dynamoDbService.guardarSuscripcion(suscripcion);
        cliente.setSaldo(cliente.getSaldo().add(suscripcion.getMontoVinculado()));
        dynamoDbService.guardarCliente(cliente);
        Transaccion transaccion = new Transaccion();
        transaccion.setIdentificadorTransaccion(identificadorTransaccion);
        transaccion.setClienteId(request.getClienteId());
        transaccion.setFondoId(request.getFondoId());
        transaccion.setTipo(Transaccion.TipoTransaccion.CANCELACION);
        transaccion.setMonto(suscripcion.getMontoVinculado());
        transaccion.setFechaTransaccion(LocalDateTime.now());
        transaccion.setDescripcion("Cancelación de suscripción al fondo " + fondo.getNombre());
        transaccion.setEstado(Transaccion.EstadoTransaccion.EXITOSA);
        dynamoDbService.guardarTransaccion(transaccion);
        notificacionService.enviarNotificacionCancelacion(cliente, fondo, suscripcion.getMontoVinculado());
        log.info("Cancelación exitosa: identificadorTransaccion={}", identificadorTransaccion);
        return TransaccionResponse.fromTransaccion(transaccion);
    }
    
    @Override
    public List<TransaccionResponse> obtenerHistorialTransacciones(String clienteId) {
        log.info("Obteniendo historial de transacciones para clienteId={}", clienteId);
        if (dynamoDbService.obtenerCliente(clienteId).isEmpty()) {
            throw new RecursoNoEncontradoException("Cliente no encontrado");
        }
        List<Transaccion> transacciones = dynamoDbService.obtenerTransaccionesPorCliente(clienteId);
        return transacciones.stream()
                .map(TransaccionResponse::fromTransaccion)
                .collect(Collectors.toList());
    }
    
    private String generarIdentificadorTransaccion() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
} 