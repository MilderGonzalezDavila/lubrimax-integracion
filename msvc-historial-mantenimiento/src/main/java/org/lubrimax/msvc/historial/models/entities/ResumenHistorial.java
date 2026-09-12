package org.lubrimax.msvc.historial.models.entities;

import java.time.LocalDate;
import java.util.List;

public record ResumenHistorial(
        Long vehiculoId,
        LocalDate ultimaFecha,
        Long ultimoKilometraje,
        List<ProductoUtilizadoResumen> ultimosProductos,
        ProximoServicio proximoServicio) {
    public static ResumenHistorial desde(HistorialDeMantenimiento historial) {
        return historial
                .ultimoRegistro()
                .map(
                        registro ->
                                new ResumenHistorial(
                                        historial.getVehiculoId(),
                                        registro.getFechaAtencion(),
                                        registro.getKilometraje(),
                                        registro.getProductosUtilizados(),
                                        registro.getProximoServicio()))
                .orElse(
                        new ResumenHistorial(
                                historial.getVehiculoId(), null, null, List.of(), null));
    }
}
