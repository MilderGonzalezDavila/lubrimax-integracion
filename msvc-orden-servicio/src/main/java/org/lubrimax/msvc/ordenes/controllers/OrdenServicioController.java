package org.lubrimax.msvc.ordenes.controllers;

import feign.FeignException;

import jakarta.validation.Valid;

import org.lubrimax.msvc.ordenes.models.entities.Autorizacion;
import org.lubrimax.msvc.ordenes.models.entities.OrdenServicio;
import org.lubrimax.msvc.ordenes.models.entities.PropuestaTecnica;
import org.lubrimax.msvc.ordenes.services.OrdenServicioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenServicioController {

    private final OrdenServicioService service;

    public OrdenServicioController(OrdenServicioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<OrdenServicio>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        return service.porId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody OrdenServicio orden, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(orden));
        } catch (FeignException.NotFound exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "No existe el cliente o vehículo indicado"));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", exception.getMessage()));
        }
    }

    @PostMapping("/{ordenId}/propuestas")
    public ResponseEntity<?> agregarPropuesta(
            @PathVariable Long ordenId,
            @Valid @RequestBody PropuestaTecnica propuesta,
            BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        return ejecutar(() -> service.agregarPropuesta(ordenId, propuesta), HttpStatus.CREATED);
    }

    @PutMapping("/{ordenId}/propuestas/{propuestaId}/presentar")
    public ResponseEntity<?> presentarPropuesta(
            @PathVariable Long ordenId, @PathVariable Long propuestaId) {
        return ejecutar(() -> service.presentarPropuesta(ordenId, propuestaId), HttpStatus.OK);
    }

    @PostMapping("/{ordenId}/autorizaciones")
    public ResponseEntity<?> autorizar(
            @PathVariable Long ordenId,
            @Valid @RequestBody Autorizacion autorizacion,
            BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        return ejecutar(() -> service.autorizar(ordenId, autorizacion), HttpStatus.CREATED);
    }

    @PutMapping("/{ordenId}/emitir")
    public ResponseEntity<?> emitir(@PathVariable Long ordenId) {
        return ejecutar(() -> service.emitir(ordenId), HttpStatus.OK);
    }

    private ResponseEntity<?> ejecutar(Operacion operacion, HttpStatus estado) {
        try {
            return ResponseEntity.status(estado).body(operacion.ejecutar());
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

    @FunctionalInterface
    private interface Operacion {
        OrdenServicio ejecutar();
    }
}
