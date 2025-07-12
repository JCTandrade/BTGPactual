package com.gft.BTGPactual.dto;

import com.gft.BTGPactual.model.Transaccion;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransaccionResponse {
    
    private String identificadorTransaccion;
    private String nombreCliente;
    private String nombreFondo;
    private String tipoTransaccion;
    private BigDecimal monto;
    private LocalDateTime fechaTransaccion;
    private String descripcion;
    private String estado;
    
    public static TransaccionResponse fromTransaccion(Transaccion transaccion) {
        TransaccionResponse response = new TransaccionResponse();
        response.setIdentificadorTransaccion(transaccion.getIdentificadorTransaccion());
        response.setNombreCliente(transaccion.getCliente().getNombre());
        response.setNombreFondo(transaccion.getFondo().getNombre());
        response.setTipoTransaccion(transaccion.getTipo().name());
        response.setMonto(transaccion.getMonto());
        response.setFechaTransaccion(transaccion.getFechaTransaccion());
        response.setDescripcion(transaccion.getDescripcion());
        response.setEstado(transaccion.getEstado().name());
        return response;
    }
} 