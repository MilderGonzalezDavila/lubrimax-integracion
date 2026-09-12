package org.lubrimax.msvc.inventario.services;

import org.lubrimax.msvc.inventario.models.entity.ExistenciaDeProducto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface InventarioService {
    List<ExistenciaDeProducto> listar();
    Optional<ExistenciaDeProducto> buscarPorId(Long id);
    Optional<ExistenciaDeProducto> buscarPorProductoPresentacion(Long productoId, Long presentacionId);
    void eliminarPorId(Long id);
    ExistenciaDeProducto habilitarStock(
            Long productoId,
            Long presentacionId,
            BigDecimal cantidad,
            Long recepcionId
    );

    ExistenciaDeProducto reservar(
            Long existenciaId,
            Long ordenId,
            BigDecimal cantidad
    );

    ExistenciaDeProducto consumir(
            Long existenciaId,
            Long ordenId,
            BigDecimal cantidad
    );
    ExistenciaDeProducto liberarReserva(
            Long existenciaId,
            Long ordenId
    );
    ExistenciaDeProducto confirmarReserva(
            Long existenciaId,
            Long ordenId
    );
}
