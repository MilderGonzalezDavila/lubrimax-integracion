package org.lubrimax.msvc_acopio_temporal.services;

import org.lubrimax.msvc_acopio_temporal.models.CantidadDeResiduo;
import org.lubrimax.msvc_acopio_temporal.models.CapacidadDeAcopio;
import org.lubrimax.msvc_acopio_temporal.models.TipoDeResiduo;
import org.lubrimax.msvc_acopio_temporal.models.entity.AcopioTemporal;
import org.lubrimax.msvc_acopio_temporal.models.entity.ResiduoGenerado;
import org.lubrimax.msvc_acopio_temporal.repositories.AcopioTemporalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AcopioTemporalServiceImpl implements AcopioTemporalService {

    private final AcopioTemporalRepository repository;

    public AcopioTemporalServiceImpl(AcopioTemporalRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public AcopioTemporal crear(TipoDeResiduo tipo, CapacidadDeAcopio capacidad) {
        if (repository.existsById(tipo)) {
            throw new IllegalArgumentException("Ya existe un acopio para " + tipo);
        }
        return repository.save(new AcopioTemporal(tipo, capacidad));
    }

    @Override
    @Transactional(readOnly = true)
    public AcopioTemporal obtener(TipoDeResiduo tipo) {
        return repository
                .findById(tipo)
                .orElseThrow(
                        () -> new IllegalArgumentException("No existe un acopio para " + tipo));
    }

    @Override
    @Transactional
    public AcopioTemporal configurarCapacidad(TipoDeResiduo tipo, CapacidadDeAcopio capacidad) {
        AcopioTemporal acopio = obtener(tipo);
        acopio.configurarCapacidad(capacidad);
        return repository.save(acopio);
    }

    @Override
    @Transactional
    public ResiduoGenerado registrarResiduo(
            TipoDeResiduo tipo,
            Long ordenId,
            Long declaracionOrigenId,
            CantidadDeResiduo cantidad,
            LocalDateTime fechaGeneracion) {
        AcopioTemporal acopio = obtener(tipo);
        if (declaracionOrigenId != null) {
            ResiduoGenerado existente =
                    acopio.getResiduos().stream()
                            .filter(
                                    residuo ->
                                            ordenId.equals(residuo.getOrdenId())
                                                    && declaracionOrigenId.equals(
                                                            residuo.getDeclaracionOrigenId()))
                            .findFirst()
                            .orElse(null);
            if (existente != null) {
                return existente;
            }
        }
        ResiduoGenerado residuo =
                acopio.registrarGeneracion(
                        ordenId, declaracionOrigenId, tipo, cantidad, fechaGeneracion);
        repository.save(acopio);
        return residuo;
    }

    @Override
    @Transactional
    public AcopioTemporal almacenar(TipoDeResiduo tipo, Long residuoId) {
        AcopioTemporal acopio = obtener(tipo);
        acopio.almacenar(residuoId);
        return repository.save(acopio);
    }

    @Override
    @Transactional
    public AcopioTemporal confirmarEntrega(
            TipoDeResiduo tipo, Long entregaId, List<Long> residuosIds) {
        AcopioTemporal acopio = obtener(tipo);
        acopio.confirmarEntrega(entregaId, residuosIds);
        return repository.save(acopio);
    }
}
