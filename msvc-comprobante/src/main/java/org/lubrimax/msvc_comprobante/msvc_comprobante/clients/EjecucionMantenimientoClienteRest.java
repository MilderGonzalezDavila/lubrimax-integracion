package org.lubrimax.msvc_comprobante.msvc_comprobante.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name = "ejecucion-para-facturacion", url = "${clients.ejecucion.url}")
public interface EjecucionMantenimientoClienteRest {

    @GetMapping("/api/ejecuciones/{ordenId}")
    EjecucionRemota obtener(@PathVariable Long ordenId);

    record EjecucionRemota(String estado, LiquidacionRemota liquidacion) {}

    record LiquidacionRemota(BigDecimal total, String moneda) {}
}
