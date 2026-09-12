package org.lubrimax.msvc.vehiculos.repositories;

import org.lubrimax.msvc.vehiculos.models.entities.Vehiculo;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VehiculoRepository extends CrudRepository<Vehiculo, Long> {

    Optional<Vehiculo> findByPlaca(String placa);
}
