package org.lubrimax.msvc_politicas.services;

import org.lubrimax.msvc_politicas.models.entity.PoliticaDelNegocio;
import org.lubrimax.msvc_politicas.models.entity.Promocion;

import java.util.Optional;

public interface PoliticaDelNegocioService {
    Optional<PoliticaDelNegocio> buscarPorId(Long id);

    PoliticaDelNegocio crearPolitica(PoliticaDelNegocio politica);

    PoliticaDelNegocio agregarPromocion(Long politicaId, Promocion promocion);
}
