package com.gft.BTGPactual.controller;

import com.gft.BTGPactual.model.Cliente;
import com.gft.BTGPactual.model.Fondo;
import com.gft.BTGPactual.service.IDynamoDbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    
    private final IDynamoDbService dynamoDbService;
    
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String authorities = authentication.getAuthorities().toString();
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Acceso autorizado al endpoint admin");
        response.put("username", username);
        response.put("authorities", authorities);
        response.put("timestamp", System.currentTimeMillis());
        
        log.info("Usuario {} accedió al endpoint admin con roles: {}", username, authorities);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/fondos")
    public ResponseEntity<List<Fondo>> obtenerFondos() {
        log.info("Obteniendo lista de fondos activos");
        List<Fondo> fondos = dynamoDbService.obtenerTodosLosFondos();
        return ResponseEntity.ok(fondos);
    }
    
    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> obtenerClientes() {
        log.info("Obteniendo lista de clientes activos");
        List<Cliente> clientes = dynamoDbService.obtenerTodosLosClientes();
        return ResponseEntity.ok(clientes);
    }
    
    @GetMapping("/fondos/{id}")
    public ResponseEntity<Fondo> obtenerFondo(@PathVariable String id) {
        log.info("Obteniendo fondo con id: {}", id);
        Fondo fondo = dynamoDbService.obtenerFondo(id).orElse(null);
        if (fondo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fondo);
    }
    
    @GetMapping("/clientes/{id}")
    public ResponseEntity<Cliente> obtenerCliente(@PathVariable String id) {
        log.info("Obteniendo cliente con id: {}", id);
        Cliente cliente = dynamoDbService.obtenerCliente(id).orElse(null);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }
} 