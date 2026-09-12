package org.lubrimax.msvc.clientes.services;

import org.lubrimax.msvc.clientes.models.entities.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    List<Cliente> listar();

    Optional<Cliente> porId(Long id);

    Optional<Cliente> porNumeroDocumento(String numeroDocumento);

    Cliente guardar(Cliente cliente);

    void eliminar(Long id);
}
