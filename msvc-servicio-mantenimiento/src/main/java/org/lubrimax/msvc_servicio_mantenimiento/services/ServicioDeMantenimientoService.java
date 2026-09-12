package org.lubrimax.msvc_servicio_mantenimiento.services;

import org.lubrimax.msvc_servicio_mantenimiento.models.entity.ServicioDeMantenimiento;

import java.util.List;

public interface ServicioDeMantenimientoService {

    List<ServicioDeMantenimiento> listar();

    ServicioDeMantenimiento obtener(Long id);

    ServicioDeMantenimiento guardar(ServicioDeMantenimiento servicio);

    ServicioDeMantenimiento actualizar(Long id, ServicioDeMantenimiento servicio);

    void eliminar(Long id);
}
