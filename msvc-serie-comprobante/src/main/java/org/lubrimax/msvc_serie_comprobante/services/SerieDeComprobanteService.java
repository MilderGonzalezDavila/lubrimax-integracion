package org.lubrimax.msvc_serie_comprobante.services;

import org.lubrimax.msvc_serie_comprobante.models.entity.SerieDeComprobante;

import java.util.List;

public interface SerieDeComprobanteService {

    List<SerieDeComprobante> listar();

    SerieDeComprobante obtener(Long id);

    SerieDeComprobante crear(SerieDeComprobante s);

    String siguiente(Long id);

    SerieDeComprobante actualizar(Long id, SerieDeComprobante s);
}
