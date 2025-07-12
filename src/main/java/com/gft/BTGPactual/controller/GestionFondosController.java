package com.gft.BTGPactual.controller;

import com.gft.BTGPactual.dto.CancelacionRequest;
import com.gft.BTGPactual.dto.SuscripcionRequest;
import com.gft.BTGPactual.dto.TransaccionResponse;
import com.gft.BTGPactual.service.IGestionFondosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fondos")
@RequiredArgsConstructor
@Slf4j
public class GestionFondosController {
    
    private final IGestionFondosService gestionFondosService;
    
    @PostMapping("/suscribirse")
    public ResponseEntity<TransaccionResponse> suscribirseAFondo(@Valid @RequestBody SuscripcionRequest request) {
        log.info("Recibida petición de suscripción: {}", request);
        TransaccionResponse response = gestionFondosService.suscribirseAFondo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/cancelar")
    public ResponseEntity<TransaccionResponse> cancelarSuscripcion(@Valid @RequestBody CancelacionRequest request) {
        log.info("Recibida petición de cancelación: {}", request);
        TransaccionResponse response = gestionFondosService.cancelarSuscripcion(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/transacciones/{clienteId}")
    public ResponseEntity<List<TransaccionResponse>> obtenerHistorialTransacciones(@PathVariable String clienteId) {
        log.info("Recibida petición de historial de transacciones para clienteId: {}", clienteId);
        List<TransaccionResponse> transacciones = gestionFondosService.obtenerHistorialTransacciones(clienteId);
        return ResponseEntity.ok(transacciones);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("API de Gestión de Fondos BTG Pactual funcionando correctamente");
    }
} 