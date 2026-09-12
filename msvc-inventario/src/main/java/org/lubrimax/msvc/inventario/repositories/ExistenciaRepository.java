package org.lubrimax.msvc.inventario.repositories;

import org.lubrimax.msvc.inventario.models.entity.ExistenciaDeProducto;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ExistenciaRepository extends CrudRepository<ExistenciaDeProducto, Long> {

    Optional<ExistenciaDeProducto> findByProductoIdAndPresentacionId(
            Long productoId, Long presentacionId);
}
