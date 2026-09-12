package org.lubrimax.msvc_lista_precio_base.repositories;

import org.lubrimax.msvc_lista_precio_base.models.entity.ListaDePreciosBase;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ListaDePreciosBaseRepository extends CrudRepository<ListaDePreciosBase, Long> {
    Optional<ListaDePreciosBase> findByVigenteTrue();
}
