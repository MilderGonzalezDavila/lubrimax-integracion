package org.lubrimax.msvc_serie_comprobante.services;

import org.lubrimax.msvc_serie_comprobante.models.entity.SerieDeComprobante;
import org.lubrimax.msvc_serie_comprobante.repositories.SerieDeComprobanteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class SerieDeComprobanteServiceImpl implements SerieDeComprobanteService {

    private final SerieDeComprobanteRepository repository;

    public SerieDeComprobanteServiceImpl(SerieDeComprobanteRepository r) {
        repository = r;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SerieDeComprobante> listar() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SerieDeComprobante obtener(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Serie no encontrada"));
    }

    @Override
    @Transactional
    public SerieDeComprobante crear(SerieDeComprobante s) {
        validar(s);
        s.setCorrelativoActual(0);
        return repository.save(s);
    }

    @Override
    @Transactional
    public String siguiente(Long id) {
        SerieDeComprobante s = obtener(id);
        String numero = s.siguienteNumero();
        repository.save(s);
        return numero;
    }

    @Override
    @Transactional
    public SerieDeComprobante actualizar(Long id, SerieDeComprobante d) {
        SerieDeComprobante s = obtener(id);
        if (d.getRangoFin() < s.getCorrelativoActual()) {
            throw new IllegalArgumentException("El rango no puede quedar debajo del correlativo");
        }
        s.setRangoInicio(d.getRangoInicio());
        s.setRangoFin(d.getRangoFin());
        s.setActiva(d.isActiva());
        validar(s);
        return repository.save(s);
    }

    private void validar(SerieDeComprobante s) {
        if (s.getRangoInicio() < 1 || s.getRangoFin() < s.getRangoInicio()) {
            throw new IllegalArgumentException("Rango de numeracion invalido");
        }
    }
}
