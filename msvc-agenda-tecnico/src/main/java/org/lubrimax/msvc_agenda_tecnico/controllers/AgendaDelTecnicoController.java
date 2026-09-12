package org.lubrimax.msvc_agenda_tecnico.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.lubrimax.msvc_agenda_tecnico.models.PeriodoDeTrabajo;
import org.lubrimax.msvc_agenda_tecnico.models.entity.AgendaDelTecnico;
import org.lubrimax.msvc_agenda_tecnico.models.entity.AsignacionDeTrabajo;
import org.lubrimax.msvc_agenda_tecnico.services.AgendaDelTecnicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/agendas")
public class AgendaDelTecnicoController {

    private final AgendaDelTecnicoService service;

    public AgendaDelTecnicoController(AgendaDelTecnicoService service) {
        this.service = service;
    }

    @GetMapping("/{usuarioId}")
    public AgendaDelTecnico obtener(@PathVariable Long usuarioId) {
        return service.obtener(usuarioId);
    }

    @PostMapping("/{usuarioId}/asignaciones")
    public ResponseEntity<AsignacionDeTrabajo> asignar(
            @PathVariable Long usuarioId, @Valid @RequestBody AsignarTrabajoRequest request) {
        AsignacionDeTrabajo asignacion =
                service.asignar(
                        usuarioId,
                        request.ordenId(),
                        new PeriodoDeTrabajo(request.inicio(), request.fin()));
        return ResponseEntity.status(HttpStatus.CREATED).body(asignacion);
    }

    @GetMapping("/{usuarioId}/disponibilidad")
    public DisponibilidadResponse consultarDisponibilidad(
            @PathVariable Long usuarioId,
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fin) {
        return new DisponibilidadResponse(
                service.consultarDisponibilidad(usuarioId, new PeriodoDeTrabajo(inicio, fin)));
    }

    @PostMapping("/{usuarioId}/asignaciones/{asignacionId}/iniciar")
    public AgendaDelTecnico iniciar(@PathVariable Long usuarioId, @PathVariable Long asignacionId) {
        return service.iniciarAsignacion(usuarioId, asignacionId);
    }

    @PostMapping("/{usuarioId}/asignaciones/{asignacionId}/liberar")
    public AgendaDelTecnico liberar(@PathVariable Long usuarioId, @PathVariable Long asignacionId) {
        return service.liberarAsignacion(usuarioId, asignacionId);
    }

    @GetMapping("/{usuarioId}/asignaciones")
    public List<AsignacionDeTrabajo> listarPorTecnico(@PathVariable Long usuarioId) {
        return service.listarPorTecnico(usuarioId);
    }

    @GetMapping("/asignaciones")
    public List<AsignacionDeTrabajo> listarPorFecha(@RequestParam LocalDate fecha) {
        return service.listarPorFecha(fecha);
    }

    @GetMapping("/{usuarioId}/asignaciones/orden/{ordenId}/valida")
    public ValidacionAsignacionResponse validarAsignacion(
            @PathVariable Long usuarioId, @PathVariable Long ordenId) {
        return new ValidacionAsignacionResponse(service.tieneAsignacionValida(usuarioId, ordenId));
    }

    public record AsignarTrabajoRequest(
            @NotNull Long ordenId, @NotNull LocalDateTime inicio, @NotNull LocalDateTime fin) {}

    public record DisponibilidadResponse(boolean disponible) {}

    public record ValidacionAsignacionResponse(boolean valida) {}
}
