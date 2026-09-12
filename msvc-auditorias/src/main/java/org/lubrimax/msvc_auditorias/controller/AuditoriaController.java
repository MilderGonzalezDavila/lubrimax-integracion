package org.lubrimax.msvc_auditorias.controller;

import org.lubrimax.msvc_auditorias.models.entity.AuditoriaDeControlInterno;
import org.lubrimax.msvc_auditorias.models.entity.HallazgoDeAuditoria;
import org.lubrimax.msvc_auditorias.services.AuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auditorias")
public class AuditoriaController {

    @Autowired
    private AuditoriaService service;

    @PostMapping
    public ResponseEntity<AuditoriaDeControlInterno> inicializarAuditoria(@RequestBody AuditoriaDeControlInterno auditoria) {
        auditoria.inicializar();
        AuditoriaDeControlInterno nuevaAuditoria = service.crearAuditoria(auditoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaAuditoria);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditoriaDeControlInterno> consultarEstado(@PathVariable Long id) {
        Optional<AuditoriaDeControlInterno> auditoria = service.buscarPorId(id);
        if (auditoria.isPresent()) {
            return ResponseEntity.ok(auditoria.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/hallazgos")
    public ResponseEntity<?> reportarHallazgo(@PathVariable Long id, @RequestBody HallazgoDeAuditoria hallazgo) {
        try {
            service.registrarHallazgoExterno(id, hallazgo);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/cierre")
    public ResponseEntity<?> finalizarAuditoria(@PathVariable Long id) {
        try {
            AuditoriaDeControlInterno auditoriaCerrada = service.cerrarAuditoria(id);
            return ResponseEntity.ok(auditoriaCerrada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
