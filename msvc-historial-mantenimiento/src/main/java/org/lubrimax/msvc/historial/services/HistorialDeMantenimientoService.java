package org.lubrimax.msvc.historial.services;

import org.lubrimax.msvc.historial.models.entities.HistorialDeMantenimiento;
import org.lubrimax.msvc.historial.models.entities.RegistroMantenimiento;

import java.util.List;
import java.util.Optional;

public interface HistorialDeMantenimientoService {

    List<HistorialDeMantenimiento> listar();

    Optional<HistorialDeMantenimiento> porVehiculoId(Long vehiculoId);

    HistorialDeMantenimiento registrarMantenimiento(
            Long vehiculoId, RegistroMantenimiento registro);
}
