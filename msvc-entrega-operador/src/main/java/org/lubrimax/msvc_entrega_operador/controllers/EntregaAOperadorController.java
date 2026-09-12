package org.lubrimax.msvc_entrega_operador.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.lubrimax.msvc_entrega_operador.models.CantidadDeResiduo;
import org.lubrimax.msvc_entrega_operador.models.ManifiestoDeResiduos;
import org.lubrimax.msvc_entrega_operador.models.TipoDeResiduo;
import org.lubrimax.msvc_entrega_operador.models.entity.EntregaAOperador;
import org.lubrimax.msvc_entrega_operador.services.EntregaAOperadorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/entregas")
public class EntregaAOperadorController {

    private final EntregaAOperadorService service;

    public EntregaAOperadorController(EntregaAOperadorService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EntregaAOperador> programar(
            @Valid @RequestBody ProgramarEntregaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        service.programar(
                                request.operadorId(),
                                request.usuarioResponsableId(),
                                request.fechaEntrega()));
    }

    @GetMapping("/{entregaId}")
    public EntregaAOperador obtener(@PathVariable Long entregaId) {
        return service.obtener(entregaId);
    }

    @GetMapping
    public List<EntregaAOperador> listar() {
        return service.listar();
    }

    @PostMapping("/{entregaId}/lineas")
    public EntregaAOperador agregarLinea(
            @PathVariable Long entregaId, @Valid @RequestBody AgregarLineaRequest request) {
        return service.agregarLinea(
                entregaId,
                request.tipoResiduo(),
                new CantidadDeResiduo(request.cantidad(), request.unidad()),
                request.residuosIds());
    }

    @DeleteMapping("/{entregaId}/lineas/{lineaId}")
    public EntregaAOperador eliminarLinea(
            @PathVariable Long entregaId, @PathVariable Long lineaId) {
        return service.eliminarLinea(entregaId, lineaId);
    }

    @PostMapping("/{entregaId}/manifiesto")
    public EntregaAOperador registrarManifiesto(
            @PathVariable Long entregaId, @Valid @RequestBody RegistrarManifiestoRequest request) {
        return service.registrarManifiesto(
                entregaId,
                new ManifiestoDeResiduos(
                        request.numero(),
                        request.fecha(),
                        request.cantidadTotal(),
                        request.unidad(),
                        request.responsableRecepcion()));
    }

    @PostMapping("/{entregaId}/ejecutar")
    public EntregaAOperador ejecutar(@PathVariable Long entregaId) {
        return service.ejecutar(entregaId);
    }

    @PostMapping("/{entregaId}/conformar")
    public EntregaAOperador conformar(@PathVariable Long entregaId) {
        return service.conformar(entregaId);
    }

    public record ProgramarEntregaRequest(
            @NotNull Long operadorId,
            @NotNull Long usuarioResponsableId,
            @NotNull LocalDate fechaEntrega) {}

    public record AgregarLineaRequest(
            @NotNull TipoDeResiduo tipoResiduo,
            @NotNull @Positive BigDecimal cantidad,
            @NotBlank String unidad,
            @NotEmpty List<Long> residuosIds) {}

    public record RegistrarManifiestoRequest(
            @NotBlank String numero,
            @NotNull LocalDate fecha,
            @NotNull @Positive BigDecimal cantidadTotal,
            @NotBlank String unidad,
            @NotBlank String responsableRecepcion) {}
}
