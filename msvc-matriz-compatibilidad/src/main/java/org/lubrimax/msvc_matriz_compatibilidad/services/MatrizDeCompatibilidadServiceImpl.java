package org.lubrimax.msvc_matriz_compatibilidad.services;

import org.lubrimax.msvc_matriz_compatibilidad.models.entity.MatrizDeCompatibilidad;
import org.lubrimax.msvc_matriz_compatibilidad.repositories.MatrizDeCompatibilidadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

@Service
public class MatrizDeCompatibilidadServiceImpl implements MatrizDeCompatibilidadService {

    private final MatrizDeCompatibilidadRepository repository;

    public MatrizDeCompatibilidadServiceImpl(MatrizDeCompatibilidadRepository r) {
        repository = r;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatrizDeCompatibilidad> listar() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MatrizDeCompatibilidad obtener(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Matriz no encontrada"));
    }

    @Override
    @Transactional
    public MatrizDeCompatibilidad guardar(MatrizDeCompatibilidad m) {
        if (m.getReglas().isEmpty()) {
            throw new IllegalArgumentException("La matriz requiere reglas");
        }
        return repository.save(m);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean esCompatible(Long id, String m, Integer c, String co, Integer a, Long p) {
        return obtener(id).esCompatible(m, c, co, a, p);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Long> productos(Long id, String m, Integer c, String co, Integer a) {
        return obtener(id).productosPara(m, c, co, a);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
