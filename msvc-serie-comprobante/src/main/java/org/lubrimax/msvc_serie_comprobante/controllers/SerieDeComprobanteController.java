package org.lubrimax.msvc_serie_comprobante.controllers;

import jakarta.validation.Valid;

import org.lubrimax.msvc_serie_comprobante.models.entity.SerieDeComprobante;
import org.lubrimax.msvc_serie_comprobante.services.SerieDeComprobanteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/series")
public class SerieDeComprobanteController {

    private final SerieDeComprobanteService service;

    public SerieDeComprobanteController(SerieDeComprobanteService s) {
        service = s;
    }

    @GetMapping
    public List<SerieDeComprobante> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public SerieDeComprobante obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    public ResponseEntity<SerieDeComprobante> crear(@Valid @RequestBody SerieDeComprobante s) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(s));
    }

    @PutMapping("/{id}")
    public SerieDeComprobante actualizar(
            @PathVariable Long id, @Valid @RequestBody SerieDeComprobante s) {
        return service.actualizar(id, s);
    }

    @PostMapping("/{id}/siguiente")
    public Map<String, String> siguiente(@PathVariable Long id) {
        return Map.of("numero", service.siguiente(id));
    }
}
