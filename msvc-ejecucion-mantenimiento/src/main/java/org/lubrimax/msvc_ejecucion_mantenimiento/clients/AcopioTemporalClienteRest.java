package org.lubrimax.msvc_ejecucion_mantenimiento.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@FeignClient(name = "msvc-acopio-temporal", url = "${services.acopio-temporal.url}")
public interface AcopioTemporalClienteRest {

    @PostMapping("/api/acopios/{tipo}/residuos")
    void registrarResiduo(@PathVariable("tipo") String tipo, @RequestBody RegistrarResiduoRequest request);

    record RegistrarResiduoRequest(Long ordenId, Long declaracionOrigenId, BigDecimal cantidad,
                                    String unidad, LocalDateTime fechaGeneracion) {}
}
