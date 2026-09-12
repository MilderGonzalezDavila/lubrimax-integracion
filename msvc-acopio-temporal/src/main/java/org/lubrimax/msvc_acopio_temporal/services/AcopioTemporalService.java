package org.lubrimax.msvc_acopio_temporal.services;

import org.lubrimax.msvc_acopio_temporal.models.CantidadDeResiduo;
import org.lubrimax.msvc_acopio_temporal.models.CapacidadDeAcopio;
import org.lubrimax.msvc_acopio_temporal.models.TipoDeResiduo;
import org.lubrimax.msvc_acopio_temporal.models.entity.AcopioTemporal;
import org.lubrimax.msvc_acopio_temporal.models.entity.ResiduoGenerado;

import java.time.LocalDateTime;
import java.util.List;

public interface AcopioTemporalService {
    AcopioTemporal crear(TipoDeResiduo tipo, CapacidadDeAcopio capacidad);
    AcopioTemporal obtener(TipoDeResiduo tipo);
    AcopioTemporal configurarCapacidad(TipoDeResiduo tipo, CapacidadDeAcopio capacidad);
    ResiduoGenerado registrarResiduo(TipoDeResiduo tipo, Long ordenId, Long declaracionOrigenId,
                                     CantidadDeResiduo cantidad, LocalDateTime fechaGeneracion);
    AcopioTemporal almacenar(TipoDeResiduo tipo, Long residuoId);
    AcopioTemporal confirmarEntrega(TipoDeResiduo tipo, Long entregaId, List<Long> residuosIds);
}
