package org.lubrimax.msvc_comprobante.msvc_comprobante.repositories;

import org.lubrimax.msvc_comprobante.msvc_comprobante.models.EstadoDelComprobante;
import org.lubrimax.msvc_comprobante.msvc_comprobante.models.entity.Comprobante;
import org.springframework.data.repository.CrudRepository;

public interface ComprobanteRepository extends CrudRepository<Comprobante, Long> {

    boolean existsByOrdenIdAndEstadoNot(Long ordenId, EstadoDelComprobante estado);
}
