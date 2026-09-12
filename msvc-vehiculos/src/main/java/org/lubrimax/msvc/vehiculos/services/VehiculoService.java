package org.lubrimax.msvc.vehiculos.services;

import org.lubrimax.msvc.vehiculos.models.entities.Vehiculo;

import java.util.List;
import java.util.Optional;

public interface VehiculoService {

    List<Vehiculo> listar();

    Optional<Vehiculo> porId(Long id);

    Optional<Vehiculo> porPlaca(String placa);

    Vehiculo guardar(Vehiculo vehiculo);

    void eliminar(Long id);
}
