package org.lubrimax.msvc_comprobante.msvc_comprobante.services;

import org.lubrimax.msvc_comprobante.msvc_comprobante.clients.EjecucionMantenimientoClienteRest;
import org.lubrimax.msvc_comprobante.msvc_comprobante.clients.SerieComprobanteClienteRest;
import org.lubrimax.msvc_comprobante.msvc_comprobante.models.EstadoDelComprobante;
import org.lubrimax.msvc_comprobante.msvc_comprobante.models.entity.Comprobante;
import org.lubrimax.msvc_comprobante.msvc_comprobante.models.entity.Pago;
import org.lubrimax.msvc_comprobante.msvc_comprobante.repositories.ComprobanteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ComprobanteServiceImpl implements ComprobanteService {

    private final ComprobanteRepository repository;
    private final EjecucionMantenimientoClienteRest ejecucion;
    private final SerieComprobanteClienteRest serie;

    public ComprobanteServiceImpl(
            ComprobanteRepository repository,
            EjecucionMantenimientoClienteRest ejecucion,
            SerieComprobanteClienteRest serie) {
        this.repository = repository;
        this.ejecucion = ejecucion;
        this.serie = serie;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comprobante> listar() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Comprobante obtener(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comprobante no encontrado"));
    }

    @Override
    @Transactional
    public Comprobante emitir(Comprobante comprobante) {
        if (repository.existsByOrdenIdAndEstadoNot(
                comprobante.getOrdenId(), EstadoDelComprobante.ANULADO)) {
            throw new IllegalStateException("La orden ya tiene un comprobante vigente");
        }
        EjecucionMantenimientoClienteRest.EjecucionRemota ejecucionRemota =
                ejecucion.obtener(comprobante.getOrdenId());
        if (!"CERRADA".equals(ejecucionRemota.estado()) || ejecucionRemota.liquidacion() == null) {
            throw new IllegalStateException("El servicio debe estar cerrado y liquidado");
        }
        String numero = serie.siguiente(comprobante.getSerieId()).get("numero");
        comprobante.emitir(numero, new BigDecimal("0.18"), ejecucionRemota.liquidacion().total());
        comprobante.setMoneda(ejecucionRemota.liquidacion().moneda());
        return repository.save(comprobante);
    }

    @Override
    @Transactional
    public Comprobante registrarPago(Long id, Pago pago) {
        Comprobante comprobante = obtener(id);
        comprobante.registrarPago(pago);
        return repository.save(comprobante);
    }

    @Override
    @Transactional
    public Comprobante anular(Long id) {
        Comprobante comprobante = obtener(id);
        comprobante.anular();
        return repository.save(comprobante);
    }
}
