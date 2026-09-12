package org.lubrimax.msvc_politicas.controllers;

import org.lubrimax.msvc_politicas.models.entity.PoliticaDelNegocio;
import org.lubrimax.msvc_politicas.models.entity.Promocion;
import org.lubrimax.msvc_politicas.services.PoliticaDelNegocioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/politicas")
public class PoliticaDelNegocioController {

    @Autowired private PoliticaDelNegocioService service;

    @GetMapping("/{id}")
    public ResponseEntity<PoliticaDelNegocio> obtenerPorId(@PathVariable Long id) {
        Optional<PoliticaDelNegocio> politica = service.buscarPorId(id);
        if (politica.isPresent()) {
            return ResponseEntity.ok(politica.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PoliticaDelNegocio> crearPolitica(
            @RequestBody PoliticaDelNegocio politica) {
        PoliticaDelNegocio nuevaPolitica = service.crearPolitica(politica);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPolitica);
    }

    @PutMapping("/{id}/promociones")
    public ResponseEntity<?> agregarPromocion(
            @PathVariable Long id, @RequestBody Promocion promocion) {
        try {
            PoliticaDelNegocio politicaActualizada = service.agregarPromocion(id, promocion);
            return ResponseEntity.status(HttpStatus.CREATED).body(politicaActualizada);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Convierte las violaciones de reglas de negocio en errores 400
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            // Maneja el caso en que el agregado no exista en la base de datos
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
