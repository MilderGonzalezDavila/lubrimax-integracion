package org.lubrimax.msvc_ejecucion_mantenimiento.services;

import org.lubrimax.msvc_ejecucion_mantenimiento.models.CantidadDeProducto;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.LiquidacionDeServicio;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.ObservacionPreventiva;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.ProgramacionDeProximoServicio;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.TipoDeResiduoGenerado;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.TipoDeTarea;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.VolumenDeclarado;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.entity.EjecucionDeMantenimiento;

import java.util.List;

public interface EjecucionDeMantenimientoService {
    EjecucionDeMantenimiento crear(Long ordenId, Long tecnicoUsuarioId, Long kilometraje);
    EjecucionDeMantenimiento obtener(Long ordenId);
    List<EjecucionDeMantenimiento> listar();
    EjecucionDeMantenimiento iniciar(Long ordenId);
    EjecucionDeMantenimiento suspender(Long ordenId);
    EjecucionDeMantenimiento registrarTarea(Long ordenId, Long servicioId, TipoDeTarea tipo);
    EjecucionDeMantenimiento iniciarTarea(Long ordenId, Long tareaId);
    EjecucionDeMantenimiento completarTarea(Long ordenId, Long tareaId);
    EjecucionDeMantenimiento marcarTareaNoRealizada(Long ordenId, Long tareaId);
    EjecucionDeMantenimiento registrarConsumo(Long ordenId, Long productoId, CantidadDeProducto cantidad);
    EjecucionDeMantenimiento declararResiduo(Long ordenId, Long tareaId,
                                              TipoDeResiduoGenerado tipo, VolumenDeclarado volumen);
    EjecucionDeMantenimiento registrarObservacion(Long ordenId, ObservacionPreventiva observacion);
    LiquidacionDeServicio calcularLiquidacion(Long ordenId);
    EjecucionDeMantenimiento definirProximoServicio(Long ordenId, ProgramacionDeProximoServicio programacion);
    EjecucionDeMantenimiento cerrar(Long ordenId);
}
