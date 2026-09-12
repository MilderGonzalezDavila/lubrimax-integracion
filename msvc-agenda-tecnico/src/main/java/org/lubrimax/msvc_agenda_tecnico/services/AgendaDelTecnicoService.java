package org.lubrimax.msvc_agenda_tecnico.services;

import org.lubrimax.msvc_agenda_tecnico.models.PeriodoDeTrabajo;
import org.lubrimax.msvc_agenda_tecnico.models.entity.AgendaDelTecnico;
import org.lubrimax.msvc_agenda_tecnico.models.entity.AsignacionDeTrabajo;

import java.time.LocalDate;
import java.util.List;

public interface AgendaDelTecnicoService {
    AgendaDelTecnico obtener(Long usuarioId);
    AsignacionDeTrabajo asignar(Long usuarioId, Long ordenId, PeriodoDeTrabajo periodo);
    boolean consultarDisponibilidad(Long usuarioId, PeriodoDeTrabajo periodo);
    AgendaDelTecnico iniciarAsignacion(Long usuarioId, Long asignacionId);
    AgendaDelTecnico liberarAsignacion(Long usuarioId, Long asignacionId);
    List<AsignacionDeTrabajo> listarPorTecnico(Long usuarioId);
    List<AsignacionDeTrabajo> listarPorFecha(LocalDate fecha);
    boolean tieneAsignacionValida(Long usuarioId, Long ordenId);
}
