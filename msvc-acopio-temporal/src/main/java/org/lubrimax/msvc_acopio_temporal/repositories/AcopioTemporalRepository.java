package org.lubrimax.msvc_acopio_temporal.repositories;

import org.lubrimax.msvc_acopio_temporal.models.TipoDeResiduo;
import org.lubrimax.msvc_acopio_temporal.models.entity.AcopioTemporal;
import org.springframework.data.repository.CrudRepository;

public interface AcopioTemporalRepository extends CrudRepository<AcopioTemporal, TipoDeResiduo> {}
