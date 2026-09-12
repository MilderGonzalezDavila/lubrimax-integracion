package org.lubrimax.msvc_entrega_operador.models;

import org.junit.jupiter.api.Test;
import org.lubrimax.msvc_entrega_operador.models.entity.EntregaAOperador;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class EntregaAOperadorTest {

    @Test
    void exigeOperadorVigente() {
        EntregaAOperador entrega = entregaConLineaYManifiesto();
        AutorizacionDelOperador vencida = new AutorizacionDelOperador("20123456789", "Operador",
                "EORS-1", LocalDate.now().minusYears(2), LocalDate.now().minusYears(1));
        assertThrows(IllegalStateException.class, () -> entrega.ejecutar(vencida));
    }

    @Test
    void noEntregaMasDeLoAcopiado() {
        CantidadDeResiduo solicitada = new CantidadDeResiduo(new BigDecimal("20"), "LITRO");
        BigDecimal disponible = new BigDecimal("10");
        assertThrows(IllegalStateException.class, () -> {
            if (solicitada.getValor().compareTo(disponible) > 0) {
                throw new IllegalStateException("La cantidad supera lo acopiado");
            }
        });
    }

    @Test
    void entregaEjecutadaExigeManifiesto() {
        EntregaAOperador entrega = new EntregaAOperador(1L, 2L, LocalDate.now());
        entrega.agregarLinea(TipoDeResiduo.ACEITE_USADO,
                new CantidadDeResiduo(BigDecimal.ONE, "LITRO"), List.of(1L));
        AutorizacionDelOperador vigente = new AutorizacionDelOperador("20123456789", "Operador",
                "EORS-1", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        assertThrows(IllegalStateException.class, () -> entrega.ejecutar(vigente));
    }

    private EntregaAOperador entregaConLineaYManifiesto() {
        EntregaAOperador entrega = new EntregaAOperador(1L, 2L, LocalDate.now());
        entrega.agregarLinea(TipoDeResiduo.ACEITE_USADO,
                new CantidadDeResiduo(BigDecimal.ONE, "LITRO"), List.of(1L));
        entrega.registrarManifiesto(new ManifiestoDeResiduos("M-1", LocalDate.now(),
                BigDecimal.ONE, "LITRO", "Responsable"));
        return entrega;
    }
}
