package org.lubrimax.msvc.abastecimiento.repositories;

import org.lubrimax.msvc.abastecimiento.models.entity.RecepcionDeMercaderia;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecepcionRepository extends CrudRepository<RecepcionDeMercaderia, Long> {

    boolean existsByProveedorId(Long proveedorId);

    List<RecepcionDeMercaderia> findByProveedorId(Long proveedorId);

    boolean existsByProveedorIdAndDocumento(Long proveedorId, String documento);
}
