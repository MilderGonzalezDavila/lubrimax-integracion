package org.lubrimax.msvc.proveedor.repositories;

import org.lubrimax.msvc.proveedor.models.entity.Proveedor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProveedorRepository extends CrudRepository<Proveedor, Long> {
    Optional<Proveedor> findByRuc(String ruc);
}
