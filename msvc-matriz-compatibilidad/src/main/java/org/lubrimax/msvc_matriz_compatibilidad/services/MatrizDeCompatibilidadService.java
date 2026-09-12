package org.lubrimax.msvc_matriz_compatibilidad.services;

import org.lubrimax.msvc_matriz_compatibilidad.models.entity.MatrizDeCompatibilidad;

import java.util.List;
import java.util.Set;

public interface MatrizDeCompatibilidadService {

    List<MatrizDeCompatibilidad> listar();

    MatrizDeCompatibilidad obtener(Long id);

    MatrizDeCompatibilidad guardar(MatrizDeCompatibilidad matriz);

    boolean esCompatible(
            Long id, String motor, Integer cc, String combustible, Integer anio, Long productoId);

    Set<Long> productos(Long id, String motor, Integer cc, String combustible, Integer anio);

    void eliminar(Long id);
}
