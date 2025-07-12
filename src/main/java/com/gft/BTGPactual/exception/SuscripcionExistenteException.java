package com.gft.BTGPactual.exception;

public class SuscripcionExistenteException extends RuntimeException {
    
    public SuscripcionExistenteException(String fondoNombre) {
        super(String.format("Ya tiene una suscripción activa al fondo %s", fondoNombre));
    }
    
    public SuscripcionExistenteException(String message, Throwable cause) {
        super(message, cause);
    }
} 