package org.lubrimax.msvc_agenda_tecnico.services;

import org.lubrimax.msvc_agenda_tecnico.clients.UsuarioClienteRest;
import org.lubrimax.msvc_agenda_tecnico.models.PeriodoDeTrabajo;
import org.lubrimax.msvc_agenda_tecnico.models.entity.AgendaDelTecnico;
import org.lubrimax.msvc_agenda_tecnico.models.entity.AsignacionDeTrabajo;
import org.lubrimax.msvc_agenda_tecnico.repositories.AgendaDelTecnicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class AgendaDelTecnicoServiceImpl implements AgendaDelTecnicoService {

    private final AgendaDelTecnicoRepository repository;
    private final UsuarioClienteRest usuarioCliente;

    public AgendaDelTecnicoServiceImpl(
            AgendaDelTecnicoRepository repository, UsuarioClienteRest usuarioCliente) {
        this.repository = repository;
        this.usuarioCliente = usuarioCliente;
    }

    @Override
    @Transactional(readOnly = true)
    public AgendaDelTecnico obtener(Long usuarioId) {
        return repository
                .findById(usuarioId)
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "No existe una agenda para el tecnico: " + usuarioId));
    }

    @Override
    @Transactional
    public AsignacionDeTrabajo asignar(Long usuarioId, Long ordenId, PeriodoDeTrabajo periodo) {
        boolean tecnicoActivo = usuarioCliente.buscarUsuario(usuarioId).esTecnicoActivo();
        AgendaDelTecnico agenda =
                repository.findById(usuarioId).orElseGet(() -> new AgendaDelTecnico(usuarioId));
        AsignacionDeTrabajo asignacion = agenda.asignar(ordenId, periodo, tecnicoActivo);
        repository.save(agenda);
        return asignacion;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean consultarDisponibilidad(Long usuarioId, PeriodoDeTrabajo periodo) {
        return repository
                .findById(usuarioId)
                .map(agenda -> agenda.estaDisponible(periodo))
                .orElse(true);
    }

    @Override
    @Transactional
    public AgendaDelTecnico iniciarAsignacion(Long usuarioId, Long asignacionId) {
        AgendaDelTecnico agenda = obtener(usuarioId);
        agenda.iniciarAsignacion(asignacionId);
        return repository.save(agenda);
    }

    @Override
    @Transactional
    public AgendaDelTecnico liberarAsignacion(Long usuarioId, Long asignacionId) {
        AgendaDelTecnico agenda = obtener(usuarioId);
        agenda.liberarAsignacion(asignacionId);
        return repository.save(agenda);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsignacionDeTrabajo> listarPorTecnico(Long usuarioId) {
        return obtener(usuarioId).getAsignaciones();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsignacionDeTrabajo> listarPorFecha(LocalDate fecha) {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .flatMap(agenda -> agenda.getAsignaciones().stream())
                .filter(
                        asignacion ->
                                !fecha.isBefore(asignacion.getPeriodo().getInicio().toLocalDate())
                                        && !fecha.isAfter(
                                                asignacion.getPeriodo().getFin().toLocalDate()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean tieneAsignacionValida(Long usuarioId, Long ordenId) {
        return repository
                .findById(usuarioId)
                .map(agenda -> agenda.tieneAsignacionValida(ordenId))
                .orElse(false);
    }
}
