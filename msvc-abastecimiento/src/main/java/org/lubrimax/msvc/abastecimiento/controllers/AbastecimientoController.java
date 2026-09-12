package org.lubrimax.msvc.abastecimiento.controllers;


import org.lubrimax.msvc.abastecimiento.models.CondicionDelInsumo;
import org.lubrimax.msvc.abastecimiento.models.EstadoDeRecepcion;
import org.lubrimax.msvc.abastecimiento.models.ResultadoDeVerificacion;

import org.lubrimax.msvc.abastecimiento.models.entity.RecepcionDeMercaderia;
import org.lubrimax.msvc.abastecimiento.services.AbastecimientoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/abastecimiento")
public class AbastecimientoController {

    private final AbastecimientoService abastecimientoService;

    // Inyección por constructor estándar
    public AbastecimientoController(AbastecimientoService abastecimientoService) {
        this.abastecimientoService = abastecimientoService;
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(abastecimientoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<RecepcionDeMercaderia> recepcionOptional = abastecimientoService.buscarPorId(id);
        if (recepcionOptional.isPresent()) {
            return ResponseEntity.ok(recepcionOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/iniciar")
    public ResponseEntity<?> iniciarRecepcion(@RequestParam Long proveedorId, @RequestParam String documento) {
        try {
            RecepcionDeMercaderia recepcion = abastecimientoService.iniciarRecepcion(proveedorId, documento);
            return ResponseEntity.status(HttpStatus.CREATED).body(recepcion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (feign.FeignException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of("error", "No se pudo consultar el proveedor para iniciar la recepción"));
        }
    }

    @PostMapping("/{id}/lineas")
    public ResponseEntity<?> agregarLineaVerificada(
            @PathVariable Long id,
            @RequestParam Long presentacion,
            @RequestParam BigDecimal cantidad,
            @RequestParam BigDecimal costo,
            @RequestParam String lote,
            @RequestParam ResultadoDeVerificacion verificacion,
            @RequestParam Long productoId
    ) {
        Optional<RecepcionDeMercaderia> recepcionOpt = abastecimientoService.buscarPorId(id);
        if (recepcionOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se encontró la recepción con ID: " + id));
        }

        try {
            RecepcionDeMercaderia recepcion = abastecimientoService.agregarLineaVerificada(
                    id, presentacion, cantidad, costo, lote, verificacion, productoId
            );
            return ResponseEntity.ok(recepcion);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/observaciones")
    public ResponseEntity<?> registrarObservacionDeLinea(
            @PathVariable Long id,
            @RequestParam Long lineaId,
            @RequestParam CondicionDelInsumo condicion) {
        Optional<RecepcionDeMercaderia> recepcionOpt = abastecimientoService.buscarPorId(id);
        if (recepcionOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se encontró la recepción con ID: " + id));
        }

        try {
            RecepcionDeMercaderia recepcion = abastecimientoService.registrarObservacionDeLinea(id, lineaId, condicion);
            return ResponseEntity.ok(recepcion);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/conformar")
    public ResponseEntity<?> darConformidad(@PathVariable Long id) {
        Optional<RecepcionDeMercaderia> recepcionOpt = abastecimientoService.buscarPorId(id);
        if (recepcionOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se encontró la recepción con ID: " + id));
        }

        try {
            RecepcionDeMercaderia recepcion = abastecimientoService.darConformidad(id);
            return ResponseEntity.ok(recepcion);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/cerrar")
    public ResponseEntity<?> cerrarRecepcion(@PathVariable Long id) {
        Optional<RecepcionDeMercaderia> recepcionOpt = abastecimientoService.buscarPorId(id);
        if (recepcionOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se encontró la recepción con ID: " + id));
        }

        if (recepcionOpt.get().getEstado() == EstadoDeRecepcion.CERRADO) {
            return ResponseEntity.ok(recepcionOpt.get());
        }

        try {
            RecepcionDeMercaderia recepcion = abastecimientoService.cerrarRecepcion(id);
            return ResponseEntity.ok(recepcion);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (feign.FeignException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of("error", "Fallo la sincronización de stock por red: " + e.getMessage()));
        }
    }

    @GetMapping("/existe-por-proveedor")
    public ResponseEntity<Boolean> existePorProveedorId(@RequestParam Long proveedorId) {
        return ResponseEntity.ok(abastecimientoService.existePorProveedorId(proveedorId));
    }
}