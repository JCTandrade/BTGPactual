package com.gft.BTGPactual.exception;

public class RecursoNoEncontradoException extends RuntimeException {
    
    public RecursoNoEncontradoException(String message) {
        super(message);
    }
    
    public RecursoNoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
} 