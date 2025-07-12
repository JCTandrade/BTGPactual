package com.gft.BTGPactual.service;

import com.gft.BTGPactual.dto.CancelacionRequest;
import com.gft.BTGPactual.dto.SuscripcionRequest;
import com.gft.BTGPactual.dto.TransaccionResponse;
import com.gft.BTGPactual.exception.RecursoNoEncontradoException;
import com.gft.BTGPactual.exception.SaldoInsuficienteException;
import com.gft.BTGPactual.exception.SuscripcionExistenteException;
import com.gft.BTGPactual.model.*;
import com.gft.BTGPactual.repository.ClienteRepository;
import com.gft.BTGPactual.repository.FondoRepository;
import com.gft.BTGPactual.repository.SuscripcionRepository;
import com.gft.BTGPactual.repository.TransaccionRepository;
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
public class GestionFondosService {
    
    private final ClienteRepository clienteRepository;
    private final FondoRepository fondoRepository;
    private final SuscripcionRepository suscripcionRepository;
    private final TransaccionRepository transaccionRepository;
    private final NotificacionService notificacionService;
    
    @Transactional
    public TransaccionResponse suscribirseAFondo(SuscripcionRequest request) {
        log.info("Iniciando suscripción: clienteId={}, fondoId={}, monto={}", 
                request.getClienteId(), request.getFondoId(), request.getMontoVinculado());
        
        // Validar que el cliente existe
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));
        
        // Validar que el fondo existe
        Fondo fondo = fondoRepository.findById(request.getFondoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Fondo no encontrado"));
        
        // Validar que no tenga una suscripción activa al mismo fondo
        if (suscripcionRepository.existsByClienteIdAndFondoIdAndEstado(
                request.getClienteId(), request.getFondoId(), Suscripcion.EstadoSuscripcion.ACTIVA)) {
            throw new SuscripcionExistenteException(fondo.getNombre());
        }
        
        // Validar monto mínimo
        if (request.getMontoVinculado().compareTo(fondo.getMontoMinimo()) < 0) {
            throw new IllegalArgumentException(
                String.format("El monto mínimo para el fondo %s es COP $%,.2f", 
                    fondo.getNombre(), fondo.getMontoMinimo())
            );
        }
        
        // Validar saldo suficiente
        if (cliente.getSaldo().compareTo(request.getMontoVinculado()) < 0) {
            throw new SaldoInsuficienteException(fondo.getNombre());
        }
        
        // Generar identificador único de transacción
        String identificadorTransaccion = generarIdentificadorTransaccion();
        
        // Crear suscripción
        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setCliente(cliente);
        suscripcion.setFondo(fondo);
        suscripcion.setMontoVinculado(request.getMontoVinculado());
        suscripcion.setFechaSuscripcion(LocalDateTime.now());
        suscripcion.setEstado(Suscripcion.EstadoSuscripcion.ACTIVA);
        suscripcion.setIdentificadorTransaccion(identificadorTransaccion);
        
        suscripcionRepository.save(suscripcion);
        
        // Actualizar saldo del cliente
        cliente.setSaldo(cliente.getSaldo().subtract(request.getMontoVinculado()));
        clienteRepository.save(cliente);
        
        // Crear transacción
        Transaccion transaccion = new Transaccion();
        transaccion.setIdentificadorTransaccion(identificadorTransaccion);
        transaccion.setCliente(cliente);
        transaccion.setFondo(fondo);
        transaccion.setTipo(Transaccion.TipoTransaccion.SUSCRIPCION);
        transaccion.setMonto(request.getMontoVinculado());
        transaccion.setFechaTransaccion(LocalDateTime.now());
        transaccion.setDescripcion("Suscripción al fondo " + fondo.getNombre());
        transaccion.setEstado(Transaccion.EstadoTransaccion.EXITOSA);
        
        transaccionRepository.save(transaccion);
        
        // Enviar notificación
        notificacionService.enviarNotificacionSuscripcion(cliente, fondo, request.getMontoVinculado());
        
        log.info("Suscripción exitosa: identificadorTransaccion={}", identificadorTransaccion);
        
        return TransaccionResponse.fromTransaccion(transaccion);
    }
    
    @Transactional
    public TransaccionResponse cancelarSuscripcion(CancelacionRequest request) {
        log.info("Iniciando cancelación: clienteId={}, fondoId={}", 
                request.getClienteId(), request.getFondoId());
        
        // Validar que el cliente existe
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));
        
        // Validar que el fondo existe
        Fondo fondo = fondoRepository.findById(request.getFondoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Fondo no encontrado"));
        
        // Buscar suscripción activa
        Suscripcion suscripcion = suscripcionRepository
                .findByClienteIdAndFondoIdAndEstado(request.getClienteId(), request.getFondoId(), 
                        Suscripcion.EstadoSuscripcion.ACTIVA)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "No se encontró una suscripción activa al fondo " + fondo.getNombre()));
        
        // Generar identificador único de transacción
        String identificadorTransaccion = generarIdentificadorTransaccion();
        
        // Cancelar suscripción
        suscripcion.setEstado(Suscripcion.EstadoSuscripcion.CANCELADA);
        suscripcion.setFechaCancelacion(LocalDateTime.now());
        suscripcionRepository.save(suscripcion);
        
        // Devolver monto al cliente
        cliente.setSaldo(cliente.getSaldo().add(suscripcion.getMontoVinculado()));
        clienteRepository.save(cliente);
        
        // Crear transacción
        Transaccion transaccion = new Transaccion();
        transaccion.setIdentificadorTransaccion(identificadorTransaccion);
        transaccion.setCliente(cliente);
        transaccion.setFondo(fondo);
        transaccion.setTipo(Transaccion.TipoTransaccion.CANCELACION);
        transaccion.setMonto(suscripcion.getMontoVinculado());
        transaccion.setFechaTransaccion(LocalDateTime.now());
        transaccion.setDescripcion("Cancelación de suscripción al fondo " + fondo.getNombre());
        transaccion.setEstado(Transaccion.EstadoTransaccion.EXITOSA);
        
        transaccionRepository.save(transaccion);
        
        // Enviar notificación
        notificacionService.enviarNotificacionCancelacion(cliente, fondo, suscripcion.getMontoVinculado());
        
        log.info("Cancelación exitosa: identificadorTransaccion={}", identificadorTransaccion);
        
        return TransaccionResponse.fromTransaccion(transaccion);
    }
    
    public List<TransaccionResponse> obtenerHistorialTransacciones(Long clienteId) {
        log.info("Obteniendo historial de transacciones para clienteId={}", clienteId);
        
        // Validar que el cliente existe
        if (!clienteRepository.existsById(clienteId)) {
            throw new RecursoNoEncontradoException("Cliente no encontrado");
        }
        
        List<Transaccion> transacciones = transaccionRepository.findByClienteIdOrderByFechaTransaccionDesc(clienteId);
        
        return transacciones.stream()
                .map(TransaccionResponse::fromTransaccion)
                .collect(Collectors.toList());
    }
    
    private String generarIdentificadorTransaccion() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
} 