package org.lubrimax.msvc.proveedor.controllers;

import feign.FeignException;

import jakarta.validation.Valid;

import org.lubrimax.msvc.proveedor.models.entity.Proveedor;
import org.lubrimax.msvc.proveedor.services.ProveedorService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public ResponseEntity<?> listarProveedores() {
        return ResponseEntity.ok(proveedorService.listarProveedores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarProveedorPorId(@PathVariable Long id) {
        Optional<Proveedor> proveedorOptional = proveedorService.buscarProveedorPorId(id);
        if (proveedorOptional.isPresent()) {
            return ResponseEntity.ok(proveedorOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> registrarProveedor(@Valid @RequestBody Proveedor proveedor) {
        if (proveedor.getId() != null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El ID se genera al registrar el proveedor"));
        }
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(proveedorService.registrarProveedor(proveedor));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error al procesar el registro del proveedor"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProveedor(
            @PathVariable Long id, @Valid @RequestBody Proveedor proveedorRequest) {
        Optional<Proveedor> proveedorOptional = proveedorService.buscarProveedorPorId(id);
        if (proveedorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Proveedor proveedorDb = proveedorOptional.get();
        proveedorDb.setRuc(proveedorRequest.getRuc());
        proveedorDb.setRazonSocial(proveedorRequest.getRazonSocial());
        proveedorDb.setContacto(proveedorRequest.getContacto());
        proveedorDb.setActivo(proveedorRequest.isActivo());
        try {
            return ResponseEntity.ok(proveedorService.registrarProveedor(proveedorDb));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error al procesar la actualización del proveedor"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProveedor(@PathVariable Long id) {
        if (proveedorService.buscarProveedorPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            proveedorService.eliminarProveedorPorId(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(
                            Map.of(
                                    "error",
                                    "No se pudo verificar el historial de recepciones del proveedor"));
        }
    }
}
