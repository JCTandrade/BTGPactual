package com.gft.BTGPactual.exception;

public class SaldoInsuficienteException extends RuntimeException {
    
    public SaldoInsuficienteException(String nombreFondo) {
        super("No tiene saldo disponible para vincularse al fondo " + nombreFondo);
    }
    
    public SaldoInsuficienteException(String message, Throwable cause) {
        super(message, cause);
    }
} 