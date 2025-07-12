package com.gft.BTGPactual.controller;

import com.gft.BTGPactual.model.Cliente;
import com.gft.BTGPactual.model.Fondo;
import com.gft.BTGPactual.repository.ClienteRepository;
import com.gft.BTGPactual.repository.FondoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    
    private final FondoRepository fondoRepository;
    private final ClienteRepository clienteRepository;
    
    @GetMapping("/fondos")
    public ResponseEntity<List<Fondo>> obtenerFondos() {
        log.info("Obteniendo lista de fondos activos");
        List<Fondo> fondos = fondoRepository.findByActivoTrue();
        return ResponseEntity.ok(fondos);
    }
    
    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> obtenerClientes() {
        log.info("Obteniendo lista de clientes activos");
        List<Cliente> clientes = clienteRepository.findAll();
        return ResponseEntity.ok(clientes);
    }
    
    @GetMapping("/fondos/{id}")
    public ResponseEntity<Fondo> obtenerFondo(@PathVariable Long id) {
        log.info("Obteniendo fondo con id: {}", id);
        Fondo fondo = fondoRepository.findById(id)
                .orElse(null);
        if (fondo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fondo);
    }
    
    @GetMapping("/clientes/{id}")
    public ResponseEntity<Cliente> obtenerCliente(@PathVariable Long id) {
        log.info("Obteniendo cliente con id: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElse(null);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }
} 