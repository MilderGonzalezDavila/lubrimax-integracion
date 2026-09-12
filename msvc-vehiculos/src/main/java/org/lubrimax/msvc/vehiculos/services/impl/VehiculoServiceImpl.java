package org.lubrimax.msvc.vehiculos.services.impl;

import org.lubrimax.msvc.vehiculos.models.entities.Vehiculo;
import org.lubrimax.msvc.vehiculos.repositories.VehiculoRepository;
import org.lubrimax.msvc.vehiculos.services.VehiculoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository repository;

    public VehiculoServiceImpl(VehiculoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehiculo> listar() {
        return (List<Vehiculo>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Vehiculo> porId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Vehiculo> porPlaca(String placa) {
        return repository.findByPlaca(placa == null ? null : placa.trim().toUpperCase(Locale.ROOT));
    }

    @Override
    @Transactional
    public Vehiculo guardar(Vehiculo vehiculo) {
        return repository.save(vehiculo);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
