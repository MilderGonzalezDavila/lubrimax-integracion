package org.lubrimax.msvc_acopio_temporal.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.lubrimax.msvc_acopio_temporal.models.CantidadDeResiduo;
import org.lubrimax.msvc_acopio_temporal.models.CapacidadDeAcopio;
import org.lubrimax.msvc_acopio_temporal.models.CondicionDeAcopio;
import org.lubrimax.msvc_acopio_temporal.models.TipoDeResiduo;
import org.lubrimax.msvc_acopio_temporal.models.entity.AcopioTemporal;
import org.lubrimax.msvc_acopio_temporal.models.entity.ResiduoGenerado;
import org.lubrimax.msvc_acopio_temporal.services.AcopioTemporalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/acopios")
public class AcopioTemporalController {

    private final AcopioTemporalService service;

    public AcopioTemporalController(AcopioTemporalService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AcopioTemporal> crear(@Valid @RequestBody CrearAcopioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        service.crear(
                                request.tipoResiduo(),
                                new CapacidadDeAcopio(request.capacidad(), request.unidad())));
    }

    @GetMapping("/{tipo}")
    public AcopioTemporal obtener(@PathVariable TipoDeResiduo tipo) {
        return service.obtener(tipo);
    }

    @PutMapping("/{tipo}/capacidad")
    public AcopioTemporal configurarCapacidad(
            @PathVariable TipoDeResiduo tipo, @Valid @RequestBody CapacidadRequest request) {
        return service.configurarCapacidad(
                tipo, new CapacidadDeAcopio(request.capacidad(), request.unidad()));
    }

    @PostMapping("/{tipo}/residuos")
    public ResponseEntity<ResiduoGenerado> registrarResiduo(
            @PathVariable TipoDeResiduo tipo, @Valid @RequestBody RegistrarResiduoRequest request) {
        ResiduoGenerado residuo =
                service.registrarResiduo(
                        tipo,
                        request.ordenId(),
                        request.declaracionOrigenId(),
                        new CantidadDeResiduo(request.cantidad(), request.unidad()),
                        request.fechaGeneracion());
        return ResponseEntity.status(HttpStatus.CREATED).body(residuo);
    }

    @PostMapping("/{tipo}/residuos/{residuoId}/almacenar")
    public AcopioTemporal almacenar(
            @PathVariable TipoDeResiduo tipo, @PathVariable Long residuoId) {
        return service.almacenar(tipo, residuoId);
    }

    @GetMapping("/{tipo}/residuos")
    public List<ResiduoGenerado> listarResiduos(@PathVariable TipoDeResiduo tipo) {
        return service.obtener(tipo).getResiduos();
    }

    @GetMapping("/{tipo}/residuos/{residuoId}")
    public ResiduoGenerado obtenerResiduo(
            @PathVariable TipoDeResiduo tipo, @PathVariable Long residuoId) {
        return service.obtener(tipo).buscarResiduo(residuoId);
    }

    @GetMapping("/{tipo}/estado")
    public EstadoAcopioResponse consultarEstado(@PathVariable TipoDeResiduo tipo) {
        AcopioTemporal acopio = service.obtener(tipo);
        return new EstadoAcopioResponse(
                tipo,
                acopio.cantidadAcopiada(),
                acopio.capacidadDisponible(),
                acopio.getCapacidad().getUnidad(),
                acopio.condicionActual());
    }

    @PostMapping("/{tipo}/entregas/confirmar")
    public AcopioTemporal confirmarEntrega(
            @PathVariable TipoDeResiduo tipo, @Valid @RequestBody ConfirmarEntregaRequest request) {
        return service.confirmarEntrega(tipo, request.entregaId(), request.residuosIds());
    }

    public record CrearAcopioRequest(
            @NotNull TipoDeResiduo tipoResiduo,
            @NotNull @Positive BigDecimal capacidad,
            @NotNull String unidad) {}

    public record CapacidadRequest(
            @NotNull @Positive BigDecimal capacidad, @NotNull String unidad) {}

    public record RegistrarResiduoRequest(
            @NotNull Long ordenId,
            Long declaracionOrigenId,
            @NotNull @Positive BigDecimal cantidad,
            @NotNull String unidad,
            LocalDateTime fechaGeneracion) {}

    public record ConfirmarEntregaRequest(
            @NotNull Long entregaId, @NotEmpty List<Long> residuosIds) {}

    public record EstadoAcopioResponse(
            TipoDeResiduo tipoResiduo,
            BigDecimal cantidadAcopiada,
            BigDecimal capacidadDisponible,
            String unidad,
            CondicionDeAcopio condicion) {}
}
