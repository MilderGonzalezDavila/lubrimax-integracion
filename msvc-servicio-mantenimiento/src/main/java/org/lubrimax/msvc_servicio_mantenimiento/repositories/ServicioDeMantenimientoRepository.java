package org.lubrimax.msvc_servicio_mantenimiento.repositories;

import org.lubrimax.msvc_servicio_mantenimiento.models.entity.ServicioDeMantenimiento;
import org.springframework.data.repository.CrudRepository;

public interface ServicioDeMantenimientoRepository
        extends CrudRepository<ServicioDeMantenimiento, Long> {}
