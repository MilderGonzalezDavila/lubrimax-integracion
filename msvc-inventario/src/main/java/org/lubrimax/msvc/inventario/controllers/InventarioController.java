package org.lubrimax.msvc.inventario.controllers;

import org.lubrimax.msvc.inventario.models.entity.ExistenciaDeProducto;
import org.lubrimax.msvc.inventario.services.InventarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(inventarioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<ExistenciaDeProducto> existenciaOptional = inventarioService.buscarPorId(id);
        if (existenciaOptional.isPresent()) {
            return ResponseEntity.ok(existenciaOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/producto/{productoId}/presentacion/{presentacionId}")
    public ResponseEntity<?> buscarPorProductoPresentacion(
            @PathVariable Long productoId, @PathVariable Long presentacionId) {
        Optional<ExistenciaDeProducto> existenciaOptional =
                inventarioService.buscarPorProductoPresentacion(productoId, presentacionId);
        if (existenciaOptional.isPresent()) {
            return ResponseEntity.ok(existenciaOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    // El evento 'StockInsumoHabilitado' de Abastecimiento te envía estos datos directo por
    // parámetros HTTP
    @PostMapping("/habilitar")
    public ResponseEntity<?> habilitarStock(
            @RequestParam Long productoId,
            @RequestParam Long presentacionId,
            @RequestParam BigDecimal cantidad,
            @RequestParam Long recepcionId) {
        try {
            ExistenciaDeProducto existencia =
                    inventarioService.habilitarStock(
                            productoId, presentacionId, cantidad, recepcionId);
            return ResponseEntity.status(HttpStatus.CREATED).body(existencia);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/reservar")
    public ResponseEntity<?> reservar(
            @PathVariable Long id, @RequestParam Long orden, @RequestParam BigDecimal cantidad) {
        try {
            ExistenciaDeProducto existencia = inventarioService.reservar(id, orden, cantidad);
            return ResponseEntity.ok(existencia);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/consumir")
    public ResponseEntity<?> consumir(
            @PathVariable Long id, @RequestParam Long orden, @RequestParam BigDecimal cantidad) {
        try {
            ExistenciaDeProducto existencia = inventarioService.consumir(id, orden, cantidad);
            return ResponseEntity.ok(existencia);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<ExistenciaDeProducto> existenciaOpt = inventarioService.buscarPorId(id);

        if (existenciaOpt.isPresent()) {
            ExistenciaDeProducto existencia = existenciaOpt.get();

            java.math.BigDecimal fisico = existencia.getSaldo().getFisico();
            java.math.BigDecimal reservado = existencia.getSaldo().getReservado();

            if (fisico.compareTo(java.math.BigDecimal.ZERO) > 0
                    || reservado.compareTo(java.math.BigDecimal.ZERO) > 0) {
                return ResponseEntity.badRequest()
                        .body(
                                Map.of(
                                        "error",
                                        "No se puede eliminar la existencia: El almacén registra saldos o reservas activas para este producto."));
            }
            inventarioService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/confirmar-reserva")
    public ResponseEntity<?> confirmarReserva(@PathVariable Long id, @RequestParam Long orden) {
        try {
            ExistenciaDeProducto existencia = inventarioService.confirmarReserva(id, orden);
            return ResponseEntity.ok(existencia);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/liberar-reserva")
    public ResponseEntity<?> liberarReserva(@PathVariable Long id, @RequestParam Long orden) {
        try {
            ExistenciaDeProducto existencia = inventarioService.liberarReserva(id, orden);
            return ResponseEntity.ok(existencia);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
