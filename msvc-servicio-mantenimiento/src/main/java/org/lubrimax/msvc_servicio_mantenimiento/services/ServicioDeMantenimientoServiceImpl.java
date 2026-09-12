package org.lubrimax.msvc_servicio_mantenimiento.services;

import org.lubrimax.msvc_servicio_mantenimiento.models.entity.ServicioDeMantenimiento;
import org.lubrimax.msvc_servicio_mantenimiento.repositories.ServicioDeMantenimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ServicioDeMantenimientoServiceImpl implements ServicioDeMantenimientoService {

    private final ServicioDeMantenimientoRepository repository;

    public ServicioDeMantenimientoServiceImpl(ServicioDeMantenimientoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioDeMantenimiento> listar() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioDeMantenimiento obtener(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Servicio no encontrado"));
    }

    @Override
    @Transactional
    public ServicioDeMantenimiento guardar(ServicioDeMantenimiento servicio) {
        validar(servicio);
        return repository.save(servicio);
    }

    @Override
    @Transactional
    public ServicioDeMantenimiento actualizar(Long id, ServicioDeMantenimiento datos) {
        ServicioDeMantenimiento actual = obtener(id);
        validar(datos);
        actual.setTipo(datos.getTipo());
        actual.setDescripcion(datos.getDescripcion());
        actual.setActivo(datos.isActivo());
        actual.setCategoriasConsumidas(datos.getCategoriasConsumidas());
        return repository.save(actual);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Servicio no encontrado");
        }
        repository.deleteById(id);
    }

    private void validar(ServicioDeMantenimiento servicio) {
        if (servicio.getTipo() == null || !servicio.getTipo().esPreventivoLigero()) {
            throw new IllegalArgumentException("Solo se admiten servicios preventivos ligeros");
        }
        if (servicio.getCategoriasConsumidas() == null
                || servicio.getCategoriasConsumidas().isEmpty()) {
            throw new IllegalArgumentException("Debe declarar las categorias que consume");
        }
    }
}
