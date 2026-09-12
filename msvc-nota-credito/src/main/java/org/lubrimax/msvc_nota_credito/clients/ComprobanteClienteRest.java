package org.lubrimax.msvc_nota_credito.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.math.BigDecimal;

@FeignClient(name = "comprobante-para-nota", url = "${clients.comprobante.url}")
public interface ComprobanteClienteRest {

    @GetMapping("/api/comprobantes/{id}")
    ComprobanteRemoto obtener(@PathVariable Long id);

    @PutMapping("/api/comprobantes/{id}/anular")
    ComprobanteRemoto anular(@PathVariable Long id);

    record ComprobanteRemoto(BigDecimal total, String moneda, String estado) {}
}
