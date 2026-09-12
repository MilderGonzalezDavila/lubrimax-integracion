package org.lubrimax.msvc.clientes.controllers;

import jakarta.validation.Valid;
import org.lubrimax.msvc.clientes.models.entities.Cliente;
import org.lubrimax.msvc.clientes.services.ClienteService;
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
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        return service.porId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Cliente cliente, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        if (service.porNumeroDocumento(cliente.getNumeroDocumento()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Ya existe un cliente con ese número de documento"));
        }
        Cliente clienteGuardado = service.guardar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteGuardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @Valid @RequestBody Cliente cliente, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }

        Optional<Cliente> clienteOpt = service.porId(id);
        if (clienteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Cliente clienteActual = clienteOpt.get();
        if (!clienteActual.getNumeroDocumento().equals(cliente.getNumeroDocumento())) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "El número de documento es inmutable"));
        }

        clienteActual.actualizarDatosDeContacto(cliente.getNombre(), cliente.getTelefono());
        return ResponseEntity.ok(service.guardar(clienteActual));
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
