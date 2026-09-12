package org.lubrimax.msvc_politicas.services;

import org.lubrimax.msvc_politicas.models.entity.PoliticaDelNegocio;
import org.lubrimax.msvc_politicas.models.entity.Promocion;
import org.lubrimax.msvc_politicas.repositories.PoliticaDelNegocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PoliticaDelNegocioServiceImpl implements PoliticaDelNegocioService {

    @Autowired private PoliticaDelNegocioRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Optional<PoliticaDelNegocio> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public PoliticaDelNegocio crearPolitica(PoliticaDelNegocio politica) {
        return repository.save(politica);
    }

    @Override
    @Transactional
    public PoliticaDelNegocio agregarPromocion(Long politicaId, Promocion promocion) {
        PoliticaDelNegocio politica =
                repository
                        .findById(politicaId)
                        .orElseThrow(() -> new RuntimeException("Política no encontrada"));
        politica.getPromociones().add(promocion);
        politica.renovar();
        return repository.save(politica);
    }
}
