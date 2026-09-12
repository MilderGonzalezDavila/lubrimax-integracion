package org.lubrimax.msvc_ejecucion_mantenimiento.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.CantidadDeProducto;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.LiquidacionDeServicio;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.ObservacionPreventiva;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.ProgramacionDeProximoServicio;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.TipoDeResiduoGenerado;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.TipoDeTarea;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.VolumenDeclarado;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.entity.EjecucionDeMantenimiento;
import org.lubrimax.msvc_ejecucion_mantenimiento.services.EjecucionDeMantenimientoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/ejecuciones")
public class EjecucionDeMantenimientoController {

    private final EjecucionDeMantenimientoService service;

    public EjecucionDeMantenimientoController(EjecucionDeMantenimientoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EjecucionDeMantenimiento> crear(@Valid @RequestBody CrearEjecucionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.crear(request.ordenId(), request.tecnicoUsuarioId(), request.kilometraje()));
    }

    @GetMapping("/{ordenId}")
    public EjecucionDeMantenimiento obtener(@PathVariable Long ordenId) { return service.obtener(ordenId); }

    @GetMapping
    public List<EjecucionDeMantenimiento> listar() { return service.listar(); }

    @PostMapping("/{ordenId}/iniciar")
    public EjecucionDeMantenimiento iniciar(@PathVariable Long ordenId) { return service.iniciar(ordenId); }

    @PostMapping("/{ordenId}/suspender")
    public EjecucionDeMantenimiento suspender(@PathVariable Long ordenId) { return service.suspender(ordenId); }

    @PostMapping("/{ordenId}/tareas")
    public EjecucionDeMantenimiento registrarTarea(@PathVariable Long ordenId,
            @Valid @RequestBody RegistrarTareaRequest request) {
        return service.registrarTarea(ordenId, request.servicioId(), request.tipo());
    }

    @PostMapping("/{ordenId}/tareas/{tareaId}/iniciar")
    public EjecucionDeMantenimiento iniciarTarea(@PathVariable Long ordenId, @PathVariable Long tareaId) {
        return service.iniciarTarea(ordenId, tareaId);
    }

    @PostMapping("/{ordenId}/tareas/{tareaId}/completar")
    public EjecucionDeMantenimiento completarTarea(@PathVariable Long ordenId, @PathVariable Long tareaId) {
        return service.completarTarea(ordenId, tareaId);
    }

    @PostMapping("/{ordenId}/tareas/{tareaId}/no-realizada")
    public EjecucionDeMantenimiento marcarNoRealizada(@PathVariable Long ordenId, @PathVariable Long tareaId) {
        return service.marcarTareaNoRealizada(ordenId, tareaId);
    }

    @PostMapping("/{ordenId}/consumos")
    public EjecucionDeMantenimiento registrarConsumo(@PathVariable Long ordenId,
            @Valid @RequestBody RegistrarConsumoRequest request) {
        return service.registrarConsumo(ordenId, request.productoId(),
                new CantidadDeProducto(request.cantidad(), request.unidad()));
    }

    @PostMapping("/{ordenId}/residuos")
    public EjecucionDeMantenimiento declararResiduo(@PathVariable Long ordenId,
            @Valid @RequestBody DeclararResiduoRequest request) {
        return service.declararResiduo(ordenId, request.tareaId(), request.tipo(),
                new VolumenDeclarado(request.volumen(), request.unidad()));
    }

    @PostMapping("/{ordenId}/observaciones")
    public EjecucionDeMantenimiento registrarObservacion(@PathVariable Long ordenId,
            @Valid @RequestBody RegistrarObservacionRequest request) {
        return service.registrarObservacion(ordenId,
                new ObservacionPreventiva(request.tipo(), request.descripcion(), null));
    }

    @PostMapping("/{ordenId}/liquidacion")
    public LiquidacionDeServicio calcularLiquidacion(@PathVariable Long ordenId) {
        return service.calcularLiquidacion(ordenId);
    }

    @PostMapping("/{ordenId}/proximo-servicio")
    public EjecucionDeMantenimiento definirProximoServicio(@PathVariable Long ordenId,
            @Valid @RequestBody ProximoServicioRequest request) {
        return service.definirProximoServicio(ordenId,
                new ProgramacionDeProximoServicio(request.fechaSugerida(), request.kilometrajeSugerido()));
    }

    @PostMapping("/{ordenId}/cerrar")
    public EjecucionDeMantenimiento cerrar(@PathVariable Long ordenId) { return service.cerrar(ordenId); }

    public record CrearEjecucionRequest(@NotNull Long ordenId, @NotNull Long tecnicoUsuarioId,
                                         @NotNull @PositiveOrZero Long kilometraje) {}
    public record RegistrarTareaRequest(@NotNull Long servicioId, @NotNull TipoDeTarea tipo) {}
    public record RegistrarConsumoRequest(@NotNull Long productoId,
                                           @NotNull @Positive BigDecimal cantidad, @NotBlank String unidad) {}
    public record DeclararResiduoRequest(@NotNull Long tareaId, @NotNull TipoDeResiduoGenerado tipo,
                                          @NotNull @Positive BigDecimal volumen, @NotBlank String unidad) {}
    public record RegistrarObservacionRequest(@NotBlank String tipo, @NotBlank String descripcion) {}
    public record ProximoServicioRequest(LocalDate fechaSugerida, @Positive Long kilometrajeSugerido) {}
}
