package org.lubrimax.msvc_ejecucion_mantenimiento.models;

import org.junit.jupiter.api.Test;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.entity.ConsumoReal;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.entity.EjecucionDeMantenimiento;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.entity.TareaEjecutada;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EjecucionDeMantenimientoTest {

    @Test
    void noIniciaSinOrdenAutorizada() {
        assertThrows(IllegalStateException.class, () -> nuevaEjecucion().iniciar(false, true));
    }

    @Test
    void noPermiteTareaNoAutorizada() {
        EjecucionDeMantenimiento ejecucion = iniciada();
        assertThrows(IllegalArgumentException.class,
                () -> ejecucion.registrarTarea(999L, TipoDeTarea.REVISION_NIVELES));
    }

    @Test
    void noPermiteProductoNoAutorizado() {
        EjecucionDeMantenimiento ejecucion = iniciada();
        assertThrows(IllegalArgumentException.class,
                () -> ejecucion.registrarConsumo(999L, new CantidadDeProducto(BigDecimal.ONE, "UNIDAD")));
    }

    @Test
    void conservaPrecioAutorizado() {
        EjecucionDeMantenimiento ejecucion = iniciada();
        ConsumoReal consumo = ejecucion.registrarConsumo(20L,
                new CantidadDeProducto(new BigDecimal("2"), "UNIDAD"));
        assertEquals(new BigDecimal("10.00"), consumo.getPrecioAplicado().getMonto());
    }

    @Test
    void noCierraConTareaEnCurso() {
        EjecucionDeMantenimiento ejecucion = iniciada();
        TareaEjecutada tarea = ejecucion.registrarTarea(10L, TipoDeTarea.REVISION_NIVELES);
        asignarId(tarea, 1L);
        ejecucion.iniciarTarea(1L);
        ejecucion.definirProximoServicio(new ProgramacionDeProximoServicio(LocalDate.now().plusMonths(6), null));
        assertThrows(IllegalStateException.class, ejecucion::cerrar);
    }

    @Test
    void noCierraSinResiduoObligatorio() {
        EjecucionDeMantenimiento ejecucion = iniciada();
        TareaEjecutada tarea = ejecucion.registrarTarea(10L, TipoDeTarea.DRENAJE_ACEITE);
        asignarId(tarea, 1L);
        ejecucion.iniciarTarea(1L);
        ejecucion.completarTarea(1L);
        ejecucion.definirProximoServicio(new ProgramacionDeProximoServicio(LocalDate.now().plusMonths(6), null));
        assertThrows(IllegalStateException.class, ejecucion::cerrar);
    }

    @Test
    void calculaLiquidacion() {
        EjecucionDeMantenimiento ejecucion = iniciada();
        TareaEjecutada tarea = ejecucion.registrarTarea(10L, TipoDeTarea.REVISION_NIVELES);
        asignarId(tarea, 1L);
        ejecucion.iniciarTarea(1L);
        ejecucion.completarTarea(1L);
        ejecucion.registrarConsumo(20L, new CantidadDeProducto(new BigDecimal("2"), "UNIDAD"));
        assertEquals(new BigDecimal("70.00"), ejecucion.calcularLiquidacion().getTotal());
    }

    @Test
    void exigeProximoServicio() {
        EjecucionDeMantenimiento ejecucion = iniciada();
        assertThrows(IllegalStateException.class, ejecucion::cerrar);
    }

    @Test
    void ejecucionCerradaEsInmutable() {
        EjecucionDeMantenimiento ejecucion = iniciada();
        ejecucion.definirProximoServicio(new ProgramacionDeProximoServicio(LocalDate.now().plusMonths(6), null));
        ejecucion.cerrar();
        assertThrows(IllegalStateException.class,
                () -> ejecucion.registrarObservacion(new ObservacionPreventiva("RUIDO", "Revisar en taller", null)));
    }

    @Test
    void observacionNoGeneraReparacion() {
        EjecucionDeMantenimiento ejecucion = iniciada();
        ejecucion.registrarObservacion(new ObservacionPreventiva("RUIDO", "Acudir a un taller especializado", null));
        assertEquals(1, ejecucion.getObservaciones().size());
        assertEquals(0, ejecucion.getTareas().size());
        assertEquals(0, ejecucion.getConsumos().size());
    }

    private EjecucionDeMantenimiento iniciada() {
        EjecucionDeMantenimiento ejecucion = nuevaEjecucion();
        ejecucion.iniciar(true, true);
        return ejecucion;
    }

    private EjecucionDeMantenimiento nuevaEjecucion() {
        LocalDateTime momento = LocalDateTime.of(2026, 9, 11, 8, 0);
        return new EjecucionDeMantenimiento(1L, 2L, new Kilometraje(50000L),
                List.of(new ServicioAutorizado(10L, new PrecioAplicado(new BigDecimal("50.00"), "PEN", momento))),
                List.of(new ProductoAutorizado(20L, new PrecioAplicado(new BigDecimal("10.00"), "PEN", momento))));
    }

    private void asignarId(TareaEjecutada tarea, Long id) {
        try {
            var campo = TareaEjecutada.class.getDeclaredField("id");
            campo.setAccessible(true);
            campo.set(tarea, id);
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError(exception);
        }
    }
}
