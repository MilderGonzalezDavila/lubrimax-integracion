package org.lubrimax.msvc_lista_precio_base.controllers;

import jakarta.validation.Valid;

import org.lubrimax.msvc_lista_precio_base.models.TipoDeReferencia;
import org.lubrimax.msvc_lista_precio_base.models.entity.ListaDePreciosBase;
import org.lubrimax.msvc_lista_precio_base.models.entity.PrecioBase;
import org.lubrimax.msvc_lista_precio_base.services.ListaDePreciosBaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/listas-precios")
public class ListaDePreciosBaseController {

    private final ListaDePreciosBaseService service;

    public ListaDePreciosBaseController(ListaDePreciosBaseService service) {
        this.service = service;
    }

    @GetMapping
    public List<ListaDePreciosBase> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ListaDePreciosBase obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    public ResponseEntity<ListaDePreciosBase> crear(@Valid @RequestBody ListaDePreciosBase lista) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(lista));
    }

    @PostMapping("/{id}/precios")
    public ListaDePreciosBase agregarPrecio(
            @PathVariable Long id, @Valid @RequestBody PrecioBase precio) {
        return service.agregarPrecio(id, precio);
    }

    @PutMapping("/{id}/activar")
    public ListaDePreciosBase activar(@PathVariable Long id) {
        return service.activar(id);
    }

    @PutMapping("/{id}/cerrar")
    public ListaDePreciosBase cerrar(@PathVariable Long id) {
        return service.cerrar(id);
    }

    @GetMapping("/vigente/precio")
    public PrecioBase precioVigente(
            @RequestParam TipoDeReferencia tipo, @RequestParam Long referenciaId) {
        return service.precioVigente(tipo, referenciaId);
    }
}
