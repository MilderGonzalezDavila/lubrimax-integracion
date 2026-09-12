package org.lubrimax.msvc_agregar_producto.controllers;

import jakarta.validation.Valid;

import org.lubrimax.msvc_agregar_producto.models.entity.PresentacionDeProducto;
import org.lubrimax.msvc_agregar_producto.models.entity.Producto;
import org.lubrimax.msvc_agregar_producto.services.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Producto> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Producto obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(producto));
    }

    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        return service.actualizar(id, producto);
    }

    @PostMapping("/{id}/presentaciones")
    public Producto agregarPresentacion(
            @PathVariable Long id, @Valid @RequestBody PresentacionDeProducto presentacion) {
        return service.agregarPresentacion(id, presentacion);
    }

    @PutMapping("/{id}/desactivar")
    public Producto desactivar(@PathVariable Long id) {
        return service.desactivar(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
