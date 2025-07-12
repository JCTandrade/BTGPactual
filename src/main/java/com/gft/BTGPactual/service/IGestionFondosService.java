package com.gft.BTGPactual.service;

import com.gft.BTGPactual.dto.CancelacionRequest;
import com.gft.BTGPactual.dto.SuscripcionRequest;
import com.gft.BTGPactual.dto.TransaccionResponse;

import java.util.List;

public interface IGestionFondosService {
    TransaccionResponse suscribirseAFondo(SuscripcionRequest request);
    TransaccionResponse cancelarSuscripcion(CancelacionRequest request);
    List<TransaccionResponse> obtenerHistorialTransacciones(String clienteId);
} 

