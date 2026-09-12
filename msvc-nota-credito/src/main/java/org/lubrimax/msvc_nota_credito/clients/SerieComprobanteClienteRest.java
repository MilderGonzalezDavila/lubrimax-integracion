package org.lubrimax.msvc_nota_credito.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(name = "serie-para-nota", url = "${clients.serie.url}")
public interface SerieComprobanteClienteRest {

    @PostMapping("/api/series/{id}/siguiente")
    Map<String, String> siguiente(@PathVariable Long id);
}
