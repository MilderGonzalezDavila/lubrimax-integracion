package org.lubrimax.msvc_ejecucion_mantenimiento.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lubrimax.msvc_ejecucion_mantenimiento.clients.AcopioTemporalClienteRest;
import org.lubrimax.msvc_ejecucion_mantenimiento.clients.AgendaTecnicoClienteRest;
import org.lubrimax.msvc_ejecucion_mantenimiento.clients.OrdenServicioClienteRest;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.entity.EjecucionDeMantenimiento;
import org.lubrimax.msvc_ejecucion_mantenimiento.repositories.EjecucionDeMantenimientoRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EjecucionDeMantenimientoServiceImplTest {

    @Mock private EjecucionDeMantenimientoRepository repository;
    @Mock private OrdenServicioClienteRest ordenCliente;
    @Mock private AgendaTecnicoClienteRest agendaCliente;
    @Mock private AcopioTemporalClienteRest acopioCliente;
    @InjectMocks private EjecucionDeMantenimientoServiceImpl service;

    @Test
    void creaEjecucionConLosConceptosAutorizados() {
        var precio = new OrdenServicioClienteRest.PrecioResponse(
                new BigDecimal("25.00"), "PEN", LocalDateTime.now());
        when(repository.existsById(1L)).thenReturn(false);
        when(ordenCliente.obtenerParaEjecucion(1L)).thenReturn(
                new OrdenServicioClienteRest.OrdenAutorizadaResponse(1L, "EMITIDA", true,
                        List.of(new OrdenServicioClienteRest.ServicioAutorizadoResponse(10L, precio)),
                        List.of(new OrdenServicioClienteRest.ProductoAutorizadoResponse(20L, precio))));
        when(agendaCliente.validarAsignacion(2L, 1L))
                .thenReturn(new AgendaTecnicoClienteRest.AsignacionResponse(true));
        when(repository.save(any(EjecucionDeMantenimiento.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        EjecucionDeMantenimiento ejecucion = service.crear(1L, 2L, 50000L);

        assertEquals(1, ejecucion.getServiciosAutorizados().size());
        assertEquals(1, ejecucion.getProductosAutorizados().size());
    }
}
