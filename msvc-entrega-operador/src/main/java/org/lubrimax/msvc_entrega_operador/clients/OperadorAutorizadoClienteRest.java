package org.lubrimax.msvc_entrega_operador.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(name = "msvc-operador-autorizado", url = "${services.operador-autorizado.url}")
public interface OperadorAutorizadoClienteRest {

    @GetMapping("/api/operadores/{id}/vigencia")
    VigenciaResponse consultarVigencia(
            @PathVariable("id") Long id, @RequestParam("fecha") LocalDate fecha);

    record VigenciaResponse(
            Long operadorId,
            String ruc,
            String razonSocial,
            String registroEors,
            LocalDate desde,
            LocalDate hasta,
            boolean vigente) {}
}
