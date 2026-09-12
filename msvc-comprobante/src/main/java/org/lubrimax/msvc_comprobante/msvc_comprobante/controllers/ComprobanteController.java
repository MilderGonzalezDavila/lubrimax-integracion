package org.lubrimax.msvc_comprobante.msvc_comprobante.controllers;

import jakarta.validation.Valid;

import org.lubrimax.msvc_comprobante.msvc_comprobante.models.entity.Comprobante;
import org.lubrimax.msvc_comprobante.msvc_comprobante.models.entity.Pago;
import org.lubrimax.msvc_comprobante.msvc_comprobante.services.ComprobanteService;
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

@RestController
@RequestMapping("/api/comprobantes")
public class ComprobanteController {

    private final ComprobanteService service;

    public ComprobanteController(ComprobanteService service) {
        this.service = service;
    }

    @GetMapping
    public List<Comprobante> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Comprobante obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    public ResponseEntity<Comprobante> emitir(@Valid @RequestBody Comprobante comprobante) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.emitir(comprobante));
    }

    @PostMapping("/{id}/pagos")
    public Comprobante pagar(@PathVariable Long id, @Valid @RequestBody Pago pago) {
        return service.registrarPago(id, pago);
    }

    @PutMapping("/{id}/anular")
    public Comprobante anular(@PathVariable Long id) {
        return service.anular(id);
    }
}
