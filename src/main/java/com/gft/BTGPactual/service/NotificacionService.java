package com.gft.BTGPactual.service;

import com.gft.BTGPactual.model.Cliente;
import com.gft.BTGPactual.model.Fondo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class NotificacionService {
    
    public void enviarNotificacionSuscripcion(Cliente cliente, Fondo fondo, BigDecimal monto) {
        String mensaje = String.format(
            "¡Felicitaciones! Se ha suscrito exitosamente al fondo %s por un monto de COP $%,.2f",
            fondo.getNombre(),
            monto
        );
        
        enviarNotificacion(cliente, mensaje, "Suscripción a Fondo de Inversión");
    }
    
    public void enviarNotificacionCancelacion(Cliente cliente, Fondo fondo, BigDecimal monto) {
        String mensaje = String.format(
            "Su suscripción al fondo %s ha sido cancelada exitosamente. Se ha devuelto COP $%,.2f a su cuenta",
            fondo.getNombre(),
            monto
        );
        
        enviarNotificacion(cliente, mensaje, "Cancelación de Suscripción");
    }
    
    private void enviarNotificacion(Cliente cliente, String mensaje, String asunto) {
        switch (cliente.getTipoNotificacion()) {
            case EMAIL:
                enviarEmail(cliente.getEmail(), asunto, mensaje);
                break;
            case SMS:
                enviarSMS(cliente.getTelefono(), mensaje);
                break;
            case AMBOS:
                enviarEmail(cliente.getEmail(), asunto, mensaje);
                enviarSMS(cliente.getTelefono(), mensaje);
                break;
        }
    }
    
    private void enviarEmail(String email, String asunto, String mensaje) {
        // TODO: Implementar integración con servicio de email (AWS SES, SendGrid, etc.)
        log.info("EMAIL enviado a {}: {} - {}", email, asunto, mensaje);
    }
    
    private void enviarSMS(String telefono, String mensaje) {
        // TODO: Implementar integración con servicio de SMS (AWS SNS, Twilio, etc.)
        log.info("SMS enviado a {}: {}", telefono, mensaje);
    }
} 