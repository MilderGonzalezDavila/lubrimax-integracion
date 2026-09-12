package org.lubrimax.msvc.vehiculos.controllers;

import jakarta.validation.Valid;
import org.lubrimax.msvc.vehiculos.models.entities.Vehiculo;
import org.lubrimax.msvc.vehiculos.services.VehiculoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoService service;

    public VehiculoController(VehiculoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Vehiculo>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        return service.porId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/placa/{placa}")
    public ResponseEntity<?> porPlaca(@PathVariable String placa) {
        return service.porPlaca(placa)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Vehiculo vehiculo, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        if (service.porPlaca(vehiculo.getPlaca()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Ya existe un vehículo con esa placa"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(vehiculo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @Valid @RequestBody Vehiculo vehiculo, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }

        Optional<Vehiculo> vehiculoOpt = service.porId(id);
        if (vehiculoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Vehiculo vehiculoActual = vehiculoOpt.get();
        if (!vehiculoActual.getPlaca().equals(vehiculo.getPlaca())) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "La placa es inmutable"));
        }
        if (vehiculo.getKilometrajeActual() < vehiculoActual.getKilometrajeActual()) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "El kilometraje no puede disminuir"));
        }

        vehiculoActual.actualizarDatos(
                vehiculo.getFichaTecnica(),
                vehiculo.getClienteId(),
                vehiculo.getKilometrajeActual(),
                vehiculo.getEstado()
        );
        return ResponseEntity.ok(service.guardar(vehiculoActual));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (service.porId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new LinkedHashMap<>();
        result.getFieldErrors().forEach(error ->
                errores.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errores);
    }
}
