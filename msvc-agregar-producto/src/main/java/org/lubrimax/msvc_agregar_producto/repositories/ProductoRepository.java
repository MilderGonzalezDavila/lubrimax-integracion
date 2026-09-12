package org.lubrimax.msvc_agregar_producto.repositories;

import org.lubrimax.msvc_agregar_producto.models.entity.Producto;
import org.springframework.data.repository.CrudRepository;

public interface ProductoRepository extends CrudRepository<Producto, Long> {
    boolean existsByCodigo(String codigo);
}
