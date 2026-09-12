package org.lubrimax.msvc.clientes.repositories;

import org.lubrimax.msvc.clientes.models.entities.Cliente;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClienteRepository extends CrudRepository<Cliente, Long> {

    Optional<Cliente> findByNumeroDocumento(String numeroDocumento);
}
