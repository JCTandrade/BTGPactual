package com.gft.BTGPactual.service;

import com.gft.BTGPactual.dto.SuscripcionRequest;
import com.gft.BTGPactual.exception.SaldoInsuficienteException;
import com.gft.BTGPactual.model.Cliente;
import com.gft.BTGPactual.model.Fondo;
import com.gft.BTGPactual.model.Suscripcion;
import com.gft.BTGPactual.model.Transaccion;
import com.gft.BTGPactual.service.impl.GestionFondosServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestionFondosServiceTest {

    @Mock
    private IDynamoDbService dynamoDbService;

    @Mock
    private INotificacionService notificacionService;

    @InjectMocks
    private GestionFondosServiceImpl gestionFondosService;

    private Cliente cliente;
    private Fondo fondo;
    private SuscripcionRequest suscripcionRequest;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId("1");
        cliente.setNombre("Juan Carlos");
        cliente.setEmail("juan@email.com");
        cliente.setSaldo(new BigDecimal("500000.00"));

        fondo = new Fondo();
        fondo.setId("1");
        fondo.setNombre("FPV_BTG_PACTUAL_RECAUDADORA COP");
        fondo.setMontoMinimo(new BigDecimal("75000.00"));
        fondo.setCategoria(Fondo.CategoriaFondo.FPV);

        suscripcionRequest = new SuscripcionRequest();
        suscripcionRequest.setClienteId("1");
        suscripcionRequest.setFondoId("1");
        suscripcionRequest.setMontoVinculado(new BigDecimal("100000.00"));
    }

    @Test
    void suscribirseAFondo_SaldoSuficiente_DeberiaSerExitoso() {
        // Arrange
        when(dynamoDbService.obtenerCliente("1")).thenReturn(Optional.of(cliente));
        when(dynamoDbService.obtenerFondo("1")).thenReturn(Optional.of(fondo));
        when(dynamoDbService.existeSuscripcionActiva("1", "1")).thenReturn(false);

        // Act
        var resultado = gestionFondosService.suscribirseAFondo(suscripcionRequest);

        // Assert
        assertNotNull(resultado);
        verify(dynamoDbService).guardarCliente(any(Cliente.class));
        verify(dynamoDbService).guardarSuscripcion(any(Suscripcion.class));
        verify(dynamoDbService).guardarTransaccion(any(Transaccion.class));
        verify(notificacionService).enviarNotificacionSuscripcion(any(), any(), any());
    }

    @Test
    void suscribirseAFondo_SaldoInsuficiente_DeberiaLanzarExcepcion() {
        // Arrange
        suscripcionRequest.setMontoVinculado(new BigDecimal("600000.00")); // Más que el saldo disponible
        when(dynamoDbService.obtenerCliente("1")).thenReturn(Optional.of(cliente));
        when(dynamoDbService.obtenerFondo("1")).thenReturn(Optional.of(fondo));
        when(dynamoDbService.existeSuscripcionActiva("1", "1")).thenReturn(false);

        // Act & Assert
        assertThrows(SaldoInsuficienteException.class, () -> {
            gestionFondosService.suscribirseAFondo(suscripcionRequest);
        });
    }

    @Test
    void suscribirseAFondo_MontoMenorAlMinimo_DeberiaLanzarExcepcion() {
        // Arrange
        suscripcionRequest.setMontoVinculado(new BigDecimal("50000.00")); // Menor al mínimo
        when(dynamoDbService.obtenerCliente("1")).thenReturn(Optional.of(cliente));
        when(dynamoDbService.obtenerFondo("1")).thenReturn(Optional.of(fondo));
        when(dynamoDbService.existeSuscripcionActiva("1", "1")).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            gestionFondosService.suscribirseAFondo(suscripcionRequest);
        });
    }
} 