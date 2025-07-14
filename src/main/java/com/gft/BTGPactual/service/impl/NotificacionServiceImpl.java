package com.gft.BTGPactual.service.impl;

import com.gft.BTGPactual.model.Cliente;
import com.gft.BTGPactual.model.Fondo;
import com.gft.BTGPactual.service.INotificacionService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacionServiceImpl implements INotificacionService {

    @Value("${email.sender}")
    private String emailRemitente;



    private final SnsClient snsClient;
    private final SesClient sesClient;

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

            SendEmailRequest emailRequest = SendEmailRequest.builder()
                    .destination(Destination.builder()
                            .toAddresses(email)
                            .build())
                    .message(Message.builder()
                            .subject(Content.builder().data(asunto).build())
                            .body(Body.builder().text(Content.builder().data(mensaje).build()).build())
                            .build())
                    .source(emailRemitente)
                    .build();

            sesClient.sendEmail(emailRequest);
            System.out.println("Email enviado con SES");


        log.info("📧 Email enviado a {}: {}", email, asunto);
        log.debug("Contenido del email: {}", mensaje);
    }
    
    private void enviarSMS(String telefono, String mensaje) {

            PublishRequest request = PublishRequest.builder()
                    .message(mensaje)
                    .phoneNumber(telefono)
                    .build();
            PublishResponse result = snsClient.publish(request);

        log.info("SMS enviado. MessageId: {}", result.messageId());
        log.info("📱 SMS enviado a {}: {}", telefono, mensaje);
    }
} 