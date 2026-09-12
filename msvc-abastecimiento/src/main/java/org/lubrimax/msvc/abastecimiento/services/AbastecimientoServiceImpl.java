package org.lubrimax.msvc.abastecimiento.services;

import org.lubrimax.msvc.abastecimiento.clients.InventarioClienteRest;
import org.lubrimax.msvc.abastecimiento.clients.ProveedorClienteRest;
import org.lubrimax.msvc.abastecimiento.models.CondicionDelInsumo;
import org.lubrimax.msvc.abastecimiento.models.Proveedor;
import org.lubrimax.msvc.abastecimiento.models.ResultadoDeVerificacion;
import org.lubrimax.msvc.abastecimiento.models.entity.LineaDeRecepcion;
import org.lubrimax.msvc.abastecimiento.models.entity.ObservacionDeRecepcion;
import org.lubrimax.msvc.abastecimiento.models.entity.RecepcionDeMercaderia;
import org.lubrimax.msvc.abastecimiento.repositories.RecepcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AbastecimientoServiceImpl implements AbastecimientoService {

    @Autowired private RecepcionRepository recepcionRepository;

    @Autowired private InventarioClienteRest inventarioClienteRest;

    @Autowired private ProveedorClienteRest proveedorClienteRest;

    @Override
    @Transactional(readOnly = true)
    public List<RecepcionDeMercaderia> listar() {
        return (List<RecepcionDeMercaderia>) recepcionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RecepcionDeMercaderia> buscarPorId(Long id) {
        return recepcionRepository.findById(id);
    }

    @Override
    @Transactional
    public RecepcionDeMercaderia iniciarRecepcion(Long proveedorId, String documento) {
        Proveedor proveedor;
        try {
            proveedor = proveedorClienteRest.buscarProveedorPorId(proveedorId);
        } catch (feign.FeignException.NotFound e) {
            throw new IllegalArgumentException(
                    "No se puede iniciar la recepción: El proveedor con ID "
                            + proveedorId
                            + " no existe.");
        }
        if (!proveedor.isActivo()) {
            throw new IllegalArgumentException(
                    "No se puede iniciar la recepción: El proveedor se encuentra INACTIVO.");
        }

        if (recepcionRepository.existsByProveedorIdAndDocumento(proveedorId, documento)) {
            throw new IllegalArgumentException(
                    "Ya existe una recepción con el documento "
                            + documento
                            + " para el proveedor "
                            + proveedorId);
        }
        RecepcionDeMercaderia recepcion = new RecepcionDeMercaderia(proveedorId, documento);
        return recepcionRepository.save(recepcion);
    }

    @Override
    @Transactional
    public RecepcionDeMercaderia agregarLineaVerificada(
            Long recepcionId,
            Long presentacion,
            BigDecimal cantidad,
            BigDecimal costo,
            String lote,
            ResultadoDeVerificacion verificacion,
            Long productoId) {
        RecepcionDeMercaderia recepcion =
                recepcionRepository
                        .findById(recepcionId)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "No existe la recepción con ID: " + recepcionId));

        recepcion.verificar();

        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad del producto no debe ser negativo");
        }

        if (costo == null || costo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El costo de entrada no debe ser negativo");
        }
        Optional<LineaDeRecepcion> lineaExistente =
                recepcion.getLineas().stream()
                        .filter(l -> l.getPresentacion().equals(presentacion))
                        .findFirst();

        if (lineaExistente.isPresent()) {
            LineaDeRecepcion lineaActual = lineaExistente.get();
            BigDecimal nuevaCantidad = lineaActual.getCantidadDeProducto().add(cantidad);
            lineaActual.setCantidadDeProducto(nuevaCantidad);

            lineaActual.setCostoDeEntrada(costo);
            lineaActual.setLote(lote);
            lineaActual.setVerificacion(verificacion);
        } else {
            LineaDeRecepcion nuevaLinea =
                    new LineaDeRecepcion(
                            presentacion, cantidad, costo, lote, verificacion, productoId);
            recepcion.getLineas().add(nuevaLinea);
        }

        return recepcionRepository.save(recepcion);
    }

    @Override
    @Transactional
    public RecepcionDeMercaderia registrarObservacionDeLinea(
            Long recepcionId, Long lineaId, CondicionDelInsumo condicion) {
        RecepcionDeMercaderia recepcion = recepcionRepository.findById(recepcionId).get();
        recepcion.observar();
        ObservacionDeRecepcion observacion = new ObservacionDeRecepcion(lineaId, condicion);
        recepcion.getObservaciones().add(observacion);
        return recepcionRepository.save(recepcion);
    }

    @Override
    @Transactional
    public RecepcionDeMercaderia darConformidad(Long recepcionId) {
        RecepcionDeMercaderia recepcion = recepcionRepository.findById(recepcionId).get();
        recepcion.conformar();
        return recepcionRepository.save(recepcion);
    }

    @Override
    @Transactional
    public RecepcionDeMercaderia cerrarRecepcion(Long recepcionId) {
        RecepcionDeMercaderia recepcion = recepcionRepository.findById(recepcionId).get();

        recepcion.cerrar();

        RecepcionDeMercaderia recepcionGuardada = recepcionRepository.save(recepcion);

        for (LineaDeRecepcion linea : recepcionGuardada.getLineas()) {
            if (linea.getVerificacion() == ResultadoDeVerificacion.APROBADO) {
                inventarioClienteRest.habilitarStock(
                        linea.getProductoId(),
                        linea.getPresentacion(),
                        linea.getCantidadDeProducto(),
                        recepcionGuardada.getId());
            }
        }
        return recepcionGuardada;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorProveedorId(Long proveedorId) {
        return recepcionRepository.existsByProveedorId(proveedorId);
    }
}
