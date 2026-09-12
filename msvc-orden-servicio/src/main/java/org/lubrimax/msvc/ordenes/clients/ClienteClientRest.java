package org.lubrimax.msvc.ordenes.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-clientes", url = "${clients.clientes.url}")
public interface ClienteClientRest {
    @GetMapping("/{id}")
    ClienteRemoto detalle(@PathVariable Long id);
}
