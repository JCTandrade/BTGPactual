package com.gft.BTGPactual.service;

import com.gft.BTGPactual.dto.SuscripcionRequest;
import com.gft.BTGPactual.exception.SaldoInsuficienteException;
import com.gft.BTGPactual.model.Cliente;
import com.gft.BTGPactual.model.Fondo;
import com.gft.BTGPactual.model.Suscripcion;
import com.gft.BTGPactual.model.Transaccion;
import com.gft.BTGPactual.repository.ClienteRepository;
import com.gft.BTGPactual.repository.FondoRepository;
import com.gft.BTGPactual.repository.SuscripcionRepository;
import com.gft.BTGPactual.repository.TransaccionRepository;
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
    private ClienteRepository clienteRepository;

    @Mock
    private FondoRepository fondoRepository;

    @Mock
    private SuscripcionRepository suscripcionRepository;

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private GestionFondosService gestionFondosService;

    private Cliente cliente;
    private Fondo fondo;
    private SuscripcionRequest suscripcionRequest;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Carlos");
        cliente.setEmail("juan@email.com");
        cliente.setSaldo(new BigDecimal("500000.00"));

        fondo = new Fondo();
        fondo.setId(1L);
        fondo.setNombre("FPV_BTG_PACTUAL_RECAUDADORA COP");
        fondo.setMontoMinimo(new BigDecimal("75000.00"));
        fondo.setCategoria(Fondo.CategoriaFondo.FPV);

        suscripcionRequest = new SuscripcionRequest();
        suscripcionRequest.setClienteId(1L);
        suscripcionRequest.setFondoId(1L);
        suscripcionRequest.setMontoVinculado(new BigDecimal("100000.00"));
    }

    @Test
    void suscribirseAFondo_SaldoSuficiente_DeberiaSerExitoso() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(fondoRepository.findById(1L)).thenReturn(Optional.of(fondo));
        when(suscripcionRepository.existsByClienteIdAndFondoIdAndEstado(any(), any(), any())).thenReturn(false);
        when(suscripcionRepository.save(any())).thenReturn(new Suscripcion());
        when(transaccionRepository.save(any())).thenReturn(new Transaccion());

        // Act
        var resultado = gestionFondosService.suscribirseAFondo(suscripcionRequest);

        // Assert
        assertNotNull(resultado);
        verify(clienteRepository).save(any(Cliente.class));
        verify(suscripcionRepository).save(any(Suscripcion.class));
        verify(transaccionRepository).save(any(Transaccion.class));
        verify(notificacionService).enviarNotificacionSuscripcion(any(), any(), any());
    }

    @Test
    void suscribirseAFondo_SaldoInsuficiente_DeberiaLanzarExcepcion() {
        // Arrange
        suscripcionRequest.setMontoVinculado(new BigDecimal("600000.00")); // Más que el saldo disponible
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(fondoRepository.findById(1L)).thenReturn(Optional.of(fondo));
        when(suscripcionRepository.existsByClienteIdAndFondoIdAndEstado(any(), any(), any())).thenReturn(false);

        // Act & Assert
        assertThrows(SaldoInsuficienteException.class, () -> {
            gestionFondosService.suscribirseAFondo(suscripcionRequest);
        });
    }

    @Test
    void suscribirseAFondo_MontoMenorAlMinimo_DeberiaLanzarExcepcion() {
        // Arrange
        suscripcionRequest.setMontoVinculado(new BigDecimal("50000.00")); // Menor al mínimo
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(fondoRepository.findById(1L)).thenReturn(Optional.of(fondo));
        when(suscripcionRepository.existsByClienteIdAndFondoIdAndEstado(any(), any(), any())).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            gestionFondosService.suscribirseAFondo(suscripcionRequest);
        });
    }
} 