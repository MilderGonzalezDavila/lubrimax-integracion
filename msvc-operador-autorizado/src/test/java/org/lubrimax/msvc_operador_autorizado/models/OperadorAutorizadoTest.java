package org.lubrimax.msvc_operador_autorizado.models;

import org.junit.jupiter.api.Test;
import org.lubrimax.msvc_operador_autorizado.models.entity.OperadorAutorizado;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OperadorAutorizadoTest {

    @Test
    void operadorVigentePuedeRecibir() {
        OperadorAutorizado operador = operador(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        assertTrue(operador.estaVigenteAl(LocalDate.now()));
    }

    @Test
    void operadorVencidoNoPuedeRecibir() {
        OperadorAutorizado operador = operador(LocalDate.now().minusDays(10), LocalDate.now().minusDays(1));
        assertFalse(operador.estaVigenteAl(LocalDate.now()));
    }

    @Test
    void registroEsObligatorio() {
        assertThrows(IllegalArgumentException.class,
                () -> new AutorizacionDelOperador(" ", new PeriodoDeVigencia(LocalDate.now(), LocalDate.now())));
    }

    @Test
    void periodoVigenciaEsObligatorio() {
        assertThrows(IllegalArgumentException.class, () -> new AutorizacionDelOperador("EORS-1", null));
    }

    private OperadorAutorizado operador(LocalDate desde, LocalDate hasta) {
        return new OperadorAutorizado(new Ruc("20123456789"), "Operador Cajamarca",
                new Telefono("976123456"),
                new AutorizacionDelOperador("EORS-1", new PeriodoDeVigencia(desde, hasta)));
    }
}
