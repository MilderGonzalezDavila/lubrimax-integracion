package org.lubrimax.msvc.ordenes.services;

import org.lubrimax.msvc.ordenes.models.entities.Autorizacion;
import org.lubrimax.msvc.ordenes.models.entities.OrdenServicio;
import org.lubrimax.msvc.ordenes.models.entities.PropuestaTecnica;

import java.util.List;
import java.util.Optional;

public interface OrdenServicioService {

    List<OrdenServicio> listar();

    Optional<OrdenServicio> porId(Long id);

    OrdenServicio crear(OrdenServicio orden);

    OrdenServicio agregarPropuesta(Long ordenId, PropuestaTecnica propuesta);

    OrdenServicio presentarPropuesta(Long ordenId, Long propuestaId);

    OrdenServicio autorizar(Long ordenId, Autorizacion autorizacion);

    OrdenServicio emitir(Long ordenId);
}
