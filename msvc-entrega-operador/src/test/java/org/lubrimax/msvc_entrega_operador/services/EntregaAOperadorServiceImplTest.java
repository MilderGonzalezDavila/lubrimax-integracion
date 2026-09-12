package org.lubrimax.msvc_entrega_operador.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lubrimax.msvc_entrega_operador.clients.AcopioTemporalClienteRest;
import org.lubrimax.msvc_entrega_operador.clients.OperadorAutorizadoClienteRest;
import org.lubrimax.msvc_entrega_operador.models.CantidadDeResiduo;
import org.lubrimax.msvc_entrega_operador.models.EstadoDeEntrega;
import org.lubrimax.msvc_entrega_operador.models.ManifiestoDeResiduos;
import org.lubrimax.msvc_entrega_operador.models.TipoDeResiduo;
import org.lubrimax.msvc_entrega_operador.models.entity.EntregaAOperador;
import org.lubrimax.msvc_entrega_operador.repositories.EntregaAOperadorRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntregaAOperadorServiceImplTest {

    @Mock private EntregaAOperadorRepository repository;
    @Mock private OperadorAutorizadoClienteRest operadorCliente;
    @Mock private AcopioTemporalClienteRest acopioCliente;
    @InjectMocks private EntregaAOperadorServiceImpl service;

    @Test
    void ejecutaCuandoOperadorYResiduosSonValidos() {
        LocalDate hoy = LocalDate.now();
        EntregaAOperador entrega = new EntregaAOperador(1L, 2L, hoy);
        entrega.agregarLinea(TipoDeResiduo.ACEITE_USADO,
                new CantidadDeResiduo(new BigDecimal("10"), "LITRO"), List.of(5L));
        entrega.registrarManifiesto(new ManifiestoDeResiduos("M-1", hoy,
                new BigDecimal("10"), "LITRO", "Responsable"));

        when(repository.findById(1L)).thenReturn(Optional.of(entrega));
        when(operadorCliente.consultarVigencia(1L, hoy)).thenReturn(
                new OperadorAutorizadoClienteRest.VigenciaResponse(1L, "20123456789", "Operador",
                        "EORS-1", hoy.minusDays(1), hoy.plusDays(1), true));
        when(acopioCliente.listarResiduos("ACEITE_USADO")).thenReturn(List.of(
                new AcopioTemporalClienteRest.ResiduoResponse(5L, "ACEITE_USADO",
                        new AcopioTemporalClienteRest.CantidadResponse(new BigDecimal("10"), "LITRO"),
                        "ALMACENADO", null)));
        when(repository.save(any(EntregaAOperador.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        assertEquals(EstadoDeEntrega.EJECUTADA, service.ejecutar(1L).getEstado());
    }
}
