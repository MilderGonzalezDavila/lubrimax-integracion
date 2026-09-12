package org.lubrimax.msvc.ordenes.services.impl;

import feign.FeignException;
import org.lubrimax.msvc.ordenes.clients.ClienteClientRest;
import org.lubrimax.msvc.ordenes.clients.HistorialClientRest;
import org.lubrimax.msvc.ordenes.clients.ProximoServicioRemoto;
import org.lubrimax.msvc.ordenes.clients.ResumenHistorialRemoto;
import org.lubrimax.msvc.ordenes.clients.VehiculoClientRest;
import org.lubrimax.msvc.ordenes.clients.VehiculoRemoto;
import org.lubrimax.msvc.ordenes.models.entities.Autorizacion;
import org.lubrimax.msvc.ordenes.models.entities.OrdenServicio;
import org.lubrimax.msvc.ordenes.models.entities.PropuestaTecnica;
import org.lubrimax.msvc.ordenes.models.entities.ResumenHistorialLocal;
import org.lubrimax.msvc.ordenes.repositories.OrdenServicioRepository;
import org.lubrimax.msvc.ordenes.services.OrdenServicioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrdenServicioServiceImpl implements OrdenServicioService {

    private final OrdenServicioRepository repository;
    private final ClienteClientRest clienteClient;
    private final VehiculoClientRest vehiculoClient;
    private final HistorialClientRest historialClient;

    public OrdenServicioServiceImpl(OrdenServicioRepository repository, ClienteClientRest clienteClient,
                                    VehiculoClientRest vehiculoClient, HistorialClientRest historialClient) {
        this.repository = repository;
        this.clienteClient = clienteClient;
        this.vehiculoClient = vehiculoClient;
        this.historialClient = historialClient;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenServicio> listar() {
        return (List<OrdenServicio>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdenServicio> porId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public OrdenServicio crear(OrdenServicio orden) {
        clienteClient.detalle(orden.getClienteId());
        VehiculoRemoto vehiculo = vehiculoClient.detalle(orden.getVehiculoId());
        if (!orden.getClienteId().equals(vehiculo.getClienteId())) {
            throw new IllegalArgumentException("El vehículo no pertenece al cliente indicado");
        }
        if (orden.getKilometrajeCapturado() < vehiculo.getKilometrajeActual()) {
            throw new IllegalArgumentException("El kilometraje capturado no puede ser menor al kilometraje conocido del vehículo");
        }

        try {
            ResumenHistorialRemoto resumen = historialClient.resumen(orden.getVehiculoId());
            if (resumen.getUltimoKilometraje() != null
                    && orden.getKilometrajeCapturado() < resumen.getUltimoKilometraje()) {
                throw new IllegalArgumentException("El kilometraje capturado no puede ser menor al último kilometraje del historial");
            }
            ProximoServicioRemoto proximo = resumen.getProximoServicio();
            orden.setResumenHistorial(new ResumenHistorialLocal(
                    resumen.getUltimaFecha(), resumen.getUltimoKilometraje(),
                    proximo == null ? null : proximo.getFechaSugerida(),
                    proximo == null ? null : proximo.getKilometrajeSugerido()
            ));
        } catch (FeignException.NotFound ignored) {
            orden.setResumenHistorial(new ResumenHistorialLocal());
        }

        return repository.save(orden);
    }

    @Override
    @Transactional
    public OrdenServicio agregarPropuesta(Long ordenId, PropuestaTecnica propuesta) {
        OrdenServicio orden = obtenerOrden(ordenId);
        orden.agregarPropuesta(propuesta);
        return repository.save(orden);
    }

    @Override
    @Transactional
    public OrdenServicio presentarPropuesta(Long ordenId, Long propuestaId) {
        OrdenServicio orden = obtenerOrden(ordenId);
        orden.presentarPropuesta(propuestaId);
        return repository.save(orden);
    }

    @Override
    @Transactional
    public OrdenServicio autorizar(Long ordenId, Autorizacion autorizacion) {
        OrdenServicio orden = obtenerOrden(ordenId);
        orden.registrarAutorizacion(autorizacion);
        return repository.save(orden);
    }

    @Override
    @Transactional
    public OrdenServicio emitir(Long ordenId) {
        OrdenServicio orden = obtenerOrden(ordenId);
        orden.emitir();
        return repository.save(orden);
    }

    private OrdenServicio obtenerOrden(Long ordenId) {
        return repository.findById(ordenId)
                .orElseThrow(() -> new IllegalArgumentException("No existe la orden de servicio"));
    }
}
