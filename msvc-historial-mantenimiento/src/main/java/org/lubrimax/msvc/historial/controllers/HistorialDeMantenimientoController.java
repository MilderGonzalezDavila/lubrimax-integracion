package org.lubrimax.msvc.historial.controllers;

import feign.FeignException;

import jakarta.validation.Valid;

import org.lubrimax.msvc.historial.models.entities.HistorialDeMantenimiento;
import org.lubrimax.msvc.historial.models.entities.RegistroMantenimiento;
import org.lubrimax.msvc.historial.models.entities.ResumenHistorial;
import org.lubrimax.msvc.historial.services.HistorialDeMantenimientoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/historiales")
public class HistorialDeMantenimientoController {

    private final HistorialDeMantenimientoService service;

    public HistorialDeMantenimientoController(HistorialDeMantenimientoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<HistorialDeMantenimiento>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{vehiculoId}")
    public ResponseEntity<?> detalle(@PathVariable Long vehiculoId) {
        return service.porVehiculoId(vehiculoId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{vehiculoId}/resumen")
    public ResponseEntity<?> resumen(@PathVariable Long vehiculoId) {
        return service.porVehiculoId(vehiculoId)
                .<ResponseEntity<?>>map(
                        historial -> ResponseEntity.ok(ResumenHistorial.desde(historial)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{vehiculoId}/registros")
    public ResponseEntity<?> registrarMantenimiento(
            @PathVariable Long vehiculoId,
            @Valid @RequestBody RegistroMantenimiento registro,
            BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }

        try {
            HistorialDeMantenimiento historial =
                    service.registrarMantenimiento(vehiculoId, registro);
            return ResponseEntity.status(HttpStatus.CREATED).body(historial);
        } catch (FeignException.NotFound exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "No existe el vehículo indicado"));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", exception.getMessage()));
        }
    }

    private ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new LinkedHashMap<>();
        result.getFieldErrors()
                .forEach(
                        error ->
                                errores.put(
                                        error.getField(),
                                        "El campo "
                                                + error.getField()
                                                + " "
                                                + error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errores);
    }
}
