package org.lubrimax.msvc_politicas.repositories;

import org.lubrimax.msvc_politicas.models.entity.PoliticaDelNegocio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoliticaDelNegocioRepository extends CrudRepository<PoliticaDelNegocio, Long> {
}
