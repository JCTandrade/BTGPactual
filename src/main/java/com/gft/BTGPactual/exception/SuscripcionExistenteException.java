package com.gft.BTGPactual.exception;

public class SuscripcionExistenteException extends RuntimeException {
    
    public SuscripcionExistenteException(String nombreFondo) {
        super("Ya tiene una suscripción activa al fondo " + nombreFondo);
    }
    
    public SuscripcionExistenteException(String message, Throwable cause) {
        super(message, cause);
    }
} 