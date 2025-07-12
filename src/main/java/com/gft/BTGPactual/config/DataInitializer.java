package com.gft.BTGPactual.config;

import com.gft.BTGPactual.model.Cliente;
import com.gft.BTGPactual.model.Fondo;
import com.gft.BTGPactual.repository.ClienteRepository;
import com.gft.BTGPactual.repository.FondoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final FondoRepository fondoRepository;
    private final ClienteRepository clienteRepository;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("Inicializando datos de prueba...");
        
        // Crear fondos de inversión
        crearFondos();
        
        // Crear clientes de prueba
        crearClientes();
        
        log.info("Datos de prueba inicializados correctamente");
    }
    
    private void crearFondos() {
        if (fondoRepository.count() == 0) {
            Fondo fondo1 = new Fondo();
            fondo1.setNombre("FPV_BTG_PACTUAL_RECAUDADORA COP");
            fondo1.setMontoMinimo(new BigDecimal("75000.00"));
            fondo1.setCategoria(Fondo.CategoriaFondo.FPV);
            fondoRepository.save(fondo1);
            
            Fondo fondo2 = new Fondo();
            fondo2.setNombre("FPV_BTG_PACTUAL_ECOPETROL");
            fondo2.setMontoMinimo(new BigDecimal("125000.00"));
            fondo2.setCategoria(Fondo.CategoriaFondo.FPV);
            fondoRepository.save(fondo2);
            
            Fondo fondo3 = new Fondo();
            fondo3.setNombre("DEUDAPRIVADA");
            fondo3.setMontoMinimo(new BigDecimal("50000.00"));
            fondo3.setCategoria(Fondo.CategoriaFondo.FIC);
            fondoRepository.save(fondo3);
            
            Fondo fondo4 = new Fondo();
            fondo4.setNombre("FDO-ACCIONES");
            fondo4.setMontoMinimo(new BigDecimal("250000.00"));
            fondo4.setCategoria(Fondo.CategoriaFondo.FIC);
            fondoRepository.save(fondo4);
            
            Fondo fondo5 = new Fondo();
            fondo5.setNombre("FPV_BTG_PACTUAL_DINAMICA");
            fondo5.setMontoMinimo(new BigDecimal("100000.00"));
            fondo5.setCategoria(Fondo.CategoriaFondo.FPV);
            fondoRepository.save(fondo5);
            
            log.info("5 fondos de inversión creados");
        }
    }
    
    private void crearClientes() {
        if (clienteRepository.count() == 0) {
            Cliente cliente1 = new Cliente();
            cliente1.setNombre("Juan Carlos Tamayo");
            cliente1.setEmail("juan.tamayo@email.com");
            cliente1.setTelefono("+573001234567");
            cliente1.setSaldo(new BigDecimal("500000.00"));
            cliente1.setTipoNotificacion(Cliente.TipoNotificacion.EMAIL);
            clienteRepository.save(cliente1);
            
            Cliente cliente2 = new Cliente();
            cliente2.setNombre("María González");
            cliente2.setEmail("maria.gonzalez@email.com");
            cliente2.setTelefono("+573007654321");
            cliente2.setSaldo(new BigDecimal("500000.00"));
            cliente2.setTipoNotificacion(Cliente.TipoNotificacion.SMS);
            clienteRepository.save(cliente2);
            
            Cliente cliente3 = new Cliente();
            cliente3.setNombre("Carlos Rodríguez");
            cliente3.setEmail("carlos.rodriguez@email.com");
            cliente3.setTelefono("+573001112223");
            cliente3.setSaldo(new BigDecimal("500000.00"));
            cliente3.setTipoNotificacion(Cliente.TipoNotificacion.AMBOS);
            clienteRepository.save(cliente3);
            
            log.info("3 clientes de prueba creados");
        }
    }
} 