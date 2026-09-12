package org.lubrimax.msvc.abastecimiento.services;

import org.lubrimax.msvc.abastecimiento.models.CondicionDelInsumo;
import org.lubrimax.msvc.abastecimiento.models.ResultadoDeVerificacion;

import org.lubrimax.msvc.abastecimiento.models.entity.RecepcionDeMercaderia;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AbastecimientoService {

    List<RecepcionDeMercaderia> listar();
    Optional<RecepcionDeMercaderia> buscarPorId(Long id);
    RecepcionDeMercaderia iniciarRecepcion(Long proveedorId, String documento);
    RecepcionDeMercaderia agregarLineaVerificada(
            Long recepcionId,
            Long presentacion,
            BigDecimal cantidad,
            BigDecimal costo,
            String lote,
            ResultadoDeVerificacion verificacion,
            Long productoId
    );

    RecepcionDeMercaderia registrarObservacionDeLinea(
            Long recepcionId,
            Long lineaId,
            CondicionDelInsumo condicion
    );

    RecepcionDeMercaderia darConformidad(Long recepcionId);
    RecepcionDeMercaderia cerrarRecepcion(Long recepcionId);

    boolean existePorProveedorId(Long proveedorId);
}
