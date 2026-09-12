package org.lubrimax.msvc.proveedor.services;

import org.lubrimax.msvc.proveedor.models.entity.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorService {
    List<Proveedor> listarProveedores();

    Optional<Proveedor> buscarProveedorPorId(Long id);

    Proveedor registrarProveedor(Proveedor proveedor);

    void eliminarProveedorPorId(Long id);
}
