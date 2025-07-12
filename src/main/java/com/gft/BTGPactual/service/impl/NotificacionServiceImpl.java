package com.gft.BTGPactual.service.impl;

import com.gft.BTGPactual.model.Cliente;
import com.gft.BTGPactual.model.Fondo;
import com.gft.BTGPactual.service.INotificacionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacionServiceImpl implements INotificacionService {
    
    @Override
    public void enviarNotificacionSuscripcion(Cliente cliente, Fondo fondo, BigDecimal monto) {
        log.info("Enviando notificación de suscripción a cliente: {}", cliente.getEmail());
        String mensaje = String.format(
            "Estimado %s,\n\n" +
            "Su suscripción al fondo %s por un monto de COP $%,.2f ha sido exitosa.\n" +
            "Identificador de transacción: %s\n\n" +
            "Saldo restante: COP $%,.2f\n\n" +
            "Gracias por confiar en BTG Pactual.\n" +
            "Saludos cordiales,\n" +
            "Equipo BTG Pactual",
            cliente.getNombre(),
            fondo.getNombre(),
            monto,
            "TXN-" + System.currentTimeMillis(),
            cliente.getSaldo()
        );
        if (cliente.getTipoNotificacion() == Cliente.TipoNotificacion.EMAIL || 
            cliente.getTipoNotificacion() == Cliente.TipoNotificacion.AMBOS) {
            enviarEmail(cliente.getEmail(), "Suscripción Exitosa - BTG Pactual", mensaje);
        }
        if (cliente.getTipoNotificacion() == Cliente.TipoNotificacion.SMS || 
            cliente.getTipoNotificacion() == Cliente.TipoNotificacion.AMBOS) {
            enviarSMS(cliente.getTelefono(), 
                String.format("Suscripción exitosa al fondo %s por COP $%,.2f. Saldo restante: COP $%,.2f", 
                    fondo.getNombre(), monto, cliente.getSaldo()));
        }
    }
    
    @Override
    public void enviarNotificacionCancelacion(Cliente cliente, Fondo fondo, BigDecimal monto) {
        log.info("Enviando notificación de cancelación a cliente: {}", cliente.getEmail());
        String mensaje = String.format(
            "Estimado %s,\n\n" +
            "Su cancelación de suscripción al fondo %s ha sido procesada exitosamente.\n" +
            "Monto devuelto: COP $%,.2f\n\n" +
            "Nuevo saldo: COP $%,.2f\n\n" +
            "Gracias por haber sido parte de BTG Pactual.\n" +
            "Saludos cordiales,\n" +
            "Equipo BTG Pactual",
            cliente.getNombre(),
            fondo.getNombre(),
            monto,
            cliente.getSaldo()
        );
        if (cliente.getTipoNotificacion() == Cliente.TipoNotificacion.EMAIL || 
            cliente.getTipoNotificacion() == Cliente.TipoNotificacion.AMBOS) {
            enviarEmail(cliente.getEmail(), "Cancelación Exitosa - BTG Pactual", mensaje);
        }
        if (cliente.getTipoNotificacion() == Cliente.TipoNotificacion.SMS || 
            cliente.getTipoNotificacion() == Cliente.TipoNotificacion.AMBOS) {
            enviarSMS(cliente.getTelefono(), 
                String.format("Cancelación exitosa del fondo %s. Monto devuelto: COP $%,.2f. Nuevo saldo: COP $%,.2f", 
                    fondo.getNombre(), monto, cliente.getSaldo()));
        }
    }
    
    private void enviarEmail(String email, String asunto, String mensaje) {
        log.info("📧 Email enviado a {}: {}", email, asunto);
        log.debug("Contenido del email: {}", mensaje);
    }
    
    private void enviarSMS(String telefono, String mensaje) {
        log.info("📱 SMS enviado a {}: {}", telefono, mensaje);
    }
} 