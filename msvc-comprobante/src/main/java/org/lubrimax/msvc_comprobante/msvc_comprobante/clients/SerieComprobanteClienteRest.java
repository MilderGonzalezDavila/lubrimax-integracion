package org.lubrimax.msvc_comprobante.msvc_comprobante.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(name = "serie-para-facturacion", url = "${clients.serie.url}")
public interface SerieComprobanteClienteRest {

    @PostMapping("/api/series/{id}/siguiente")
    Map<String, String> siguiente(@PathVariable Long id);
}
