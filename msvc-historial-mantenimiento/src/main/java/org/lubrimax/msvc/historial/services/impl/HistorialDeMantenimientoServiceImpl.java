package org.lubrimax.msvc.historial.services.impl;

import org.lubrimax.msvc.historial.clients.VehiculoClientRest;
import org.lubrimax.msvc.historial.models.entities.HistorialDeMantenimiento;
import org.lubrimax.msvc.historial.models.entities.RegistroMantenimiento;
import org.lubrimax.msvc.historial.repositories.HistorialDeMantenimientoRepository;
import org.lubrimax.msvc.historial.services.HistorialDeMantenimientoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class HistorialDeMantenimientoServiceImpl implements HistorialDeMantenimientoService {

    private final HistorialDeMantenimientoRepository repository;
    private final VehiculoClientRest vehiculoClient;

    public HistorialDeMantenimientoServiceImpl(
            HistorialDeMantenimientoRepository repository, VehiculoClientRest vehiculoClient) {
        this.repository = repository;
        this.vehiculoClient = vehiculoClient;
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialDeMantenimiento> listar() {
        return (List<HistorialDeMantenimiento>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HistorialDeMantenimiento> porVehiculoId(Long vehiculoId) {
        return repository.findById(vehiculoId);
    }

    @Override
    @Transactional
    public HistorialDeMantenimiento registrarMantenimiento(
            Long vehiculoId, RegistroMantenimiento registro) {
        HistorialDeMantenimiento historial =
                repository
                        .findById(vehiculoId)
                        .orElseGet(
                                () -> {
                                    vehiculoClient.detalle(vehiculoId);
                                    return new HistorialDeMantenimiento(vehiculoId);
                                });

        historial.agregarRegistro(registro);
        return repository.save(historial);
    }
}
