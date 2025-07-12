package com.gft.BTGPactual.service;

import com.gft.BTGPactual.model.Cliente;
import com.gft.BTGPactual.model.Fondo;

import java.math.BigDecimal;

public interface INotificacionService {
    void enviarNotificacionSuscripcion(Cliente cliente, Fondo fondo, BigDecimal monto);
    void enviarNotificacionCancelacion(Cliente cliente, Fondo fondo, BigDecimal monto);
} 