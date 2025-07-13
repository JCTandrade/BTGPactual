package com.gft.BTGPactual.service;

import com.gft.BTGPactual.model.Cliente;
import com.gft.BTGPactual.model.Fondo;
import com.gft.BTGPactual.model.Suscripcion;
import com.gft.BTGPactual.model.Transaccion;
import com.gft.BTGPactual.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface IDynamoDbService {
    void guardarFondo(Fondo fondo);
    Optional<Fondo> obtenerFondo(String id);
    List<Fondo> obtenerTodosLosFondos();
    void guardarCliente(Cliente cliente);
    Optional<Cliente> obtenerCliente(String id);
    List<Cliente> obtenerTodosLosClientes();
    void guardarSuscripcion(Suscripcion suscripcion);
    Optional<Suscripcion> obtenerSuscripcion(String id);
    List<Suscripcion> obtenerSuscripcionesPorCliente(String clienteId);
    boolean existeSuscripcionActiva(String clienteId, String fondoId);
    void guardarTransaccion(Transaccion transaccion);
    List<Transaccion> obtenerTransaccionesPorCliente(String clienteId);
    boolean existeTransaccion(String identificadorTransaccion);
    
    void guardarUsuario(Usuario usuario);
    Optional<Usuario> obtenerUsuarioPorUsername(String username);
    List<Usuario> obtenerTodosLosUsuarios();
    boolean existeUsuario(String username);
} 