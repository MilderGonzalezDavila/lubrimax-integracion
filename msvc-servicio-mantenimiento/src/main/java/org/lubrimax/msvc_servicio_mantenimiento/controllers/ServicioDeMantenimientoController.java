package org.lubrimax.msvc_servicio_mantenimiento.controllers;

import jakarta.validation.Valid;

import org.lubrimax.msvc_servicio_mantenimiento.models.entity.ServicioDeMantenimiento;
import org.lubrimax.msvc_servicio_mantenimiento.services.ServicioDeMantenimientoService;
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
@RequestMapping("/api/servicios-mantenimiento")
public class ServicioDeMantenimientoController {

    private final ServicioDeMantenimientoService service;

    public ServicioDeMantenimientoController(ServicioDeMantenimientoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ServicioDeMantenimiento> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ServicioDeMantenimiento obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    public ResponseEntity<ServicioDeMantenimiento> crear(
            @Valid @RequestBody ServicioDeMantenimiento valor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(valor));
    }

    @PutMapping("/{id}")
    public ServicioDeMantenimiento actualizar(
            @PathVariable Long id, @Valid @RequestBody ServicioDeMantenimiento valor) {
        return service.actualizar(id, valor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
