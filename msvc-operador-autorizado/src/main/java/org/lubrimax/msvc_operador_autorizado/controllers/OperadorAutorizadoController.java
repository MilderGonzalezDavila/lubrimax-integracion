package org.lubrimax.msvc_operador_autorizado.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.lubrimax.msvc_operador_autorizado.models.AutorizacionDelOperador;
import org.lubrimax.msvc_operador_autorizado.models.PeriodoDeVigencia;
import org.lubrimax.msvc_operador_autorizado.models.Ruc;
import org.lubrimax.msvc_operador_autorizado.models.Telefono;
import org.lubrimax.msvc_operador_autorizado.models.entity.OperadorAutorizado;
import org.lubrimax.msvc_operador_autorizado.services.OperadorAutorizadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/operadores")
public class OperadorAutorizadoController {

    private final OperadorAutorizadoService service;

    public OperadorAutorizadoController(OperadorAutorizadoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OperadorAutorizado> registrar(@Valid @RequestBody RegistrarOperadorRequest request) {
        OperadorAutorizado operador = service.registrar(
                new Ruc(request.ruc()), request.razonSocial(), new Telefono(request.telefono()),
                crearAutorizacion(request.registroEors(), request.desde(), request.hasta()));
        return ResponseEntity.status(HttpStatus.CREATED).body(operador);
    }

    @GetMapping("/{id}")
    public OperadorAutorizado obtener(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping
    public Iterable<OperadorAutorizado> listar() {
        return service.listar();
    }

    @PutMapping("/{id}")
    public OperadorAutorizado actualizarDatos(@PathVariable Long id,
                                               @Valid @RequestBody ActualizarDatosRequest request) {
        return service.actualizarDatos(id, request.razonSocial(), new Telefono(request.telefono()));
    }

    @PutMapping("/{id}/autorizacion")
    public OperadorAutorizado actualizarAutorizacion(@PathVariable Long id,
            @Valid @RequestBody ActualizarAutorizacionRequest request) {
        return service.actualizarAutorizacion(id,
                crearAutorizacion(request.registroEors(), request.desde(), request.hasta()));
    }

    @GetMapping("/{id}/vigencia")
    public VigenciaResponse consultarVigencia(@PathVariable Long id, @RequestParam LocalDate fecha) {
        OperadorAutorizado operador = service.buscarPorId(id);
        return new VigenciaResponse(operador.getId(), operador.getRuc().getNumero(),
                operador.getRazonSocial(), operador.getAutorizacion().getRegistroEors(),
                operador.getAutorizacion().getPeriodoDeVigencia().getDesde(),
                operador.getAutorizacion().getPeriodoDeVigencia().getHasta(),
                service.consultarVigencia(id, fecha));
    }

    @GetMapping("/vigentes")
    public Iterable<OperadorAutorizado> listarVigentes(
            @RequestParam(required = false) LocalDate fecha) {
        LocalDate fechaDeConsulta = fecha == null ? LocalDate.now() : fecha;
        return service.listarVigentes(fechaDeConsulta);
    }

    private AutorizacionDelOperador crearAutorizacion(String registro, LocalDate desde, LocalDate hasta) {
        return new AutorizacionDelOperador(registro, new PeriodoDeVigencia(desde, hasta));
    }

    public record RegistrarOperadorRequest(@NotBlank String ruc, @NotBlank String razonSocial,
                                            @NotBlank String telefono, @NotBlank String registroEors,
                                            @NotNull LocalDate desde, @NotNull LocalDate hasta) {}

    public record ActualizarDatosRequest(@NotBlank String razonSocial, @NotBlank String telefono) {}

    public record ActualizarAutorizacionRequest(@NotBlank String registroEors,
                                                 @NotNull LocalDate desde, @NotNull LocalDate hasta) {}

    public record VigenciaResponse(Long operadorId, String ruc, String razonSocial, String registroEors,
                                   LocalDate desde, LocalDate hasta, boolean vigente) {}
}
