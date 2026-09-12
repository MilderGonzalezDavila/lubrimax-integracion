package org.lubrimax.msvc_matriz_compatibilidad.controllers;

import jakarta.validation.Valid;

import org.lubrimax.msvc_matriz_compatibilidad.models.entity.MatrizDeCompatibilidad;
import org.lubrimax.msvc_matriz_compatibilidad.services.MatrizDeCompatibilidadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/matrices-compatibilidad")
public class MatrizDeCompatibilidadController {

    private final MatrizDeCompatibilidadService service;

    public MatrizDeCompatibilidadController(MatrizDeCompatibilidadService s) {
        service = s;
    }

    @GetMapping
    public List<MatrizDeCompatibilidad> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public MatrizDeCompatibilidad obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    public ResponseEntity<MatrizDeCompatibilidad> crear(
            @Valid @RequestBody MatrizDeCompatibilidad m) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(m));
    }

    @GetMapping("/{id}/compatible")
    public Map<String, Boolean> compatible(
            @PathVariable Long id,
            @RequestParam String motor,
            @RequestParam Integer cilindrada,
            @RequestParam String combustible,
            @RequestParam Integer anio,
            @RequestParam Long productoId) {
        return Map.of(
                "compatible",
                service.esCompatible(id, motor, cilindrada, combustible, anio, productoId));
    }

    @GetMapping("/{id}/productos")
    public Set<Long> productos(
            @PathVariable Long id,
            @RequestParam String motor,
            @RequestParam Integer cilindrada,
            @RequestParam String combustible,
            @RequestParam Integer anio) {
        return service.productos(id, motor, cilindrada, combustible, anio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
