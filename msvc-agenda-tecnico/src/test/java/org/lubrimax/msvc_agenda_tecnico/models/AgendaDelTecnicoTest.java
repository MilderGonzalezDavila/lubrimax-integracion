package org.lubrimax.msvc_agenda_tecnico.models;

import org.junit.jupiter.api.Test;
import org.lubrimax.msvc_agenda_tecnico.models.entity.AgendaDelTecnico;
import org.lubrimax.msvc_agenda_tecnico.models.entity.AsignacionDeTrabajo;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AgendaDelTecnicoTest {

    @Test
    void noPermiteAsignacionesSolapadas() {
        AgendaDelTecnico agenda = new AgendaDelTecnico(1L);
        LocalDateTime inicio = LocalDateTime.of(2026, 9, 11, 8, 0);
        agenda.asignar(10L, new PeriodoDeTrabajo(inicio, inicio.plusHours(2)), true);

        assertThrows(IllegalStateException.class,
                () -> agenda.asignar(11L, new PeriodoDeTrabajo(inicio.plusHours(1), inicio.plusHours(3)), true));
    }

    @Test
    void noPermiteUsuarioNoTecnico() {
        AgendaDelTecnico agenda = new AgendaDelTecnico(1L);
        LocalDateTime inicio = LocalDateTime.now().plusHours(1);
        assertThrows(IllegalArgumentException.class,
                () -> agenda.asignar(10L, new PeriodoDeTrabajo(inicio, inicio.plusHours(1)), false));
    }

    @Test
    void asignacionLiberadaNoReinicia() {
        LocalDateTime inicio = LocalDateTime.now().plusHours(1);
        AsignacionDeTrabajo asignacion = new AsignacionDeTrabajo(10L,
                new PeriodoDeTrabajo(inicio, inicio.plusHours(1)));
        asignacion.liberar();
        assertThrows(IllegalStateException.class, asignacion::iniciar);
    }
}
