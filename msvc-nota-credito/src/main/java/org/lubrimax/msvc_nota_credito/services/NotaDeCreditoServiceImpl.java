package org.lubrimax.msvc_nota_credito.services;

import org.lubrimax.msvc_nota_credito.clients.ComprobanteClienteRest;
import org.lubrimax.msvc_nota_credito.clients.SerieComprobanteClienteRest;
import org.lubrimax.msvc_nota_credito.models.MotivoDeNotaDeCredito;
import org.lubrimax.msvc_nota_credito.models.entity.NotaDeCredito;
import org.lubrimax.msvc_nota_credito.repositories.NotaDeCreditoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class NotaDeCreditoServiceImpl implements NotaDeCreditoService {

    private final NotaDeCreditoRepository repository;
    private final ComprobanteClienteRest comprobante;
    private final SerieComprobanteClienteRest serie;

    public NotaDeCreditoServiceImpl(
            NotaDeCreditoRepository r, ComprobanteClienteRest c, SerieComprobanteClienteRest s) {
        repository = r;
        comprobante = c;
        serie = s;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotaDeCredito> listar() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public NotaDeCredito obtener(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nota de credito no encontrada"));
    }

    @Override
    @Transactional
    public NotaDeCredito emitir(NotaDeCredito n) {
        ComprobanteClienteRest.ComprobanteRemoto c = comprobante.obtener(n.getComprobanteId());
        if ("ANULADO".equals(c.estado())) {
            throw new IllegalStateException("El comprobante ya esta anulado");
        }
        n.setMoneda(c.moneda());
        n.emitir(serie.siguiente(n.getSerieId()).get("numero"), c.total());
        NotaDeCredito guardada = repository.save(n);
        if (n.getMotivo() == MotivoDeNotaDeCredito.ANULACION
                || n.getMonto().compareTo(c.total()) == 0) {
            comprobante.anular(n.getComprobanteId());
        }
        return guardada;
    }
}
