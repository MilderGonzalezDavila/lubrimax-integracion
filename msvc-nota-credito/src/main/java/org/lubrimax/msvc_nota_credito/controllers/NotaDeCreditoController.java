package org.lubrimax.msvc_nota_credito.controllers;

import jakarta.validation.Valid;

import org.lubrimax.msvc_nota_credito.models.entity.NotaDeCredito;
import org.lubrimax.msvc_nota_credito.services.NotaDeCreditoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notas-credito")
public class NotaDeCreditoController {

    private final NotaDeCreditoService service;

    public NotaDeCreditoController(NotaDeCreditoService s) {
        service = s;
    }

    @GetMapping
    public List<NotaDeCredito> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public NotaDeCredito obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    public ResponseEntity<NotaDeCredito> emitir(@Valid @RequestBody NotaDeCredito n) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.emitir(n));
    }
}
