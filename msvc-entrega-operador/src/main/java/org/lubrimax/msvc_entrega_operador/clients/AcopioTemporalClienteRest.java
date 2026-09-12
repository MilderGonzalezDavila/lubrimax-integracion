package org.lubrimax.msvc_entrega_operador.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "msvc-acopio-temporal", url = "${services.acopio-temporal.url}")
public interface AcopioTemporalClienteRest {

    @GetMapping("/api/acopios/{tipo}/residuos")
    List<ResiduoResponse> listarResiduos(@PathVariable("tipo") String tipo);

    @PostMapping("/api/acopios/{tipo}/entregas/confirmar")
    void confirmarEntrega(@PathVariable("tipo") String tipo, @RequestBody ConfirmarEntregaRequest request);

    record CantidadResponse(BigDecimal valor, String unidad) {}
    record ResiduoResponse(Long id, String tipo, CantidadResponse cantidad, String estado, Long entregaId) {}
    record ConfirmarEntregaRequest(Long entregaId, List<Long> residuosIds) {}
}
