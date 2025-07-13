package com.gft.BTGPactual.config;

import com.gft.BTGPactual.model.Cliente;
import com.gft.BTGPactual.model.Fondo;
import com.gft.BTGPactual.service.IDynamoDbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final IDynamoDbService dynamoDbService;
    
    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("Inicializando datos de prueba en DynamoDB...");
            crearFondos();
            crearClientes();
            log.info("Datos de prueba inicializados correctamente en DynamoDB");
        } catch (Exception e) {
            log.warn("No se pudo inicializar datos en DynamoDB. Error: {}. La aplicación continuará sin datos de prueba.", e.getMessage());
            log.warn("Para usar DynamoDB, configure las credenciales de AWS en las variables de entorno o en env.properties");
        }
    }
    
    private void crearFondos() {
        List<Fondo> fondosExistentes = dynamoDbService.obtenerTodosLosFondos();
        if (fondosExistentes.isEmpty()) {
            Fondo fondo1 = new Fondo();
            fondo1.setId("1");
            fondo1.setNombre("FPV_BTG_PACTUAL_RECAUDADORA COP");
            fondo1.setMontoMinimo(new BigDecimal("75000.00"));
            fondo1.setCategoria(Fondo.CategoriaFondo.FPV);
            fondo1.setActivo(true);
            dynamoDbService.guardarFondo(fondo1);
            
            Fondo fondo2 = new Fondo();
            fondo2.setId("2");
            fondo2.setNombre("FPV_BTG_PACTUAL_ECOPETROL");
            fondo2.setMontoMinimo(new BigDecimal("125000.00"));
            fondo2.setCategoria(Fondo.CategoriaFondo.FPV);
            fondo2.setActivo(true);
            dynamoDbService.guardarFondo(fondo2);
            
            Fondo fondo3 = new Fondo();
            fondo3.setId("3");
            fondo3.setNombre("DEUDAPRIVADA");
            fondo3.setMontoMinimo(new BigDecimal("50000.00"));
            fondo3.setCategoria(Fondo.CategoriaFondo.FIC);
            fondo3.setActivo(true);
            dynamoDbService.guardarFondo(fondo3);
            
            Fondo fondo4 = new Fondo();
            fondo4.setId("4");
            fondo4.setNombre("FDO-ACCIONES");
            fondo4.setMontoMinimo(new BigDecimal("250000.00"));
            fondo4.setCategoria(Fondo.CategoriaFondo.FIC);
            fondo4.setActivo(true);
            dynamoDbService.guardarFondo(fondo4);
            
            Fondo fondo5 = new Fondo();
            fondo5.setId("5");
            fondo5.setNombre("FPV_BTG_PACTUAL_DINAMICA");
            fondo5.setMontoMinimo(new BigDecimal("100000.00"));
            fondo5.setCategoria(Fondo.CategoriaFondo.FPV);
            fondo5.setActivo(true);
            dynamoDbService.guardarFondo(fondo5);
            
            log.info("5 fondos de inversión creados en DynamoDB");
        }
    }
    
    private void crearClientes() {
        List<Cliente> clientesExistentes = dynamoDbService.obtenerTodosLosClientes();
        if (clientesExistentes.isEmpty()) {
            Cliente cliente1 = new Cliente();
            cliente1.setId("1");
            cliente1.setNombre("Juan Carlos Tamayo");
            cliente1.setEmail("juan.tamayo@email.com");
            cliente1.setTelefono("+573001234567");
            cliente1.setSaldo(new BigDecimal("500000.00"));
            cliente1.setTipoNotificacion(Cliente.TipoNotificacion.EMAIL);
            cliente1.setActivo(true);
            dynamoDbService.guardarCliente(cliente1);
            
            Cliente cliente2 = new Cliente();
            cliente2.setId("2");
            cliente2.setNombre("María González");
            cliente2.setEmail("maria.gonzalez@email.com");
            cliente2.setTelefono("+573007654321");
            cliente2.setSaldo(new BigDecimal("500000.00"));
            cliente2.setTipoNotificacion(Cliente.TipoNotificacion.SMS);
            cliente2.setActivo(true);
            dynamoDbService.guardarCliente(cliente2);
            
            Cliente cliente3 = new Cliente();
            cliente3.setId("3");
            cliente3.setNombre("Carlos Rodríguez");
            cliente3.setEmail("carlos.rodriguez@email.com");
            cliente3.setTelefono("+573001112223");
            cliente3.setSaldo(new BigDecimal("500000.00"));
            cliente3.setTipoNotificacion(Cliente.TipoNotificacion.AMBOS);
            cliente3.setActivo(true);
            dynamoDbService.guardarCliente(cliente3);
            
            log.info("3 clientes de prueba creados en DynamoDB");
        }
    }
} 