package com.gft.BTGPactual.exception;

public class SaldoInsuficienteException extends RuntimeException {
    
    public SaldoInsuficienteException(String fondoNombre) {
        super(String.format("Saldo insuficiente para suscribirse al fondo %s", fondoNombre));
    }
    
    public SaldoInsuficienteException(String message, Throwable cause) {
        super(message, cause);
    }
} 