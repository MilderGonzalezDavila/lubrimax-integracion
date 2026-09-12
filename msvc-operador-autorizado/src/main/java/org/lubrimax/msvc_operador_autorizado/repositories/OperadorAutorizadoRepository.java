package org.lubrimax.msvc_operador_autorizado.repositories;

import org.lubrimax.msvc_operador_autorizado.models.entity.OperadorAutorizado;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OperadorAutorizadoRepository extends CrudRepository<OperadorAutorizado, Long> {

    Optional<OperadorAutorizado> findByRucNumero(String ruc);

    Optional<OperadorAutorizado> findByAutorizacionRegistroEors(String registroEors);

    List<OperadorAutorizado> findByAutorizacionPeriodoDeVigenciaDesdeLessThanEqualAndAutorizacionPeriodoDeVigenciaHastaGreaterThanEqual(
            LocalDate fechaDesde, LocalDate fechaHasta);
}
