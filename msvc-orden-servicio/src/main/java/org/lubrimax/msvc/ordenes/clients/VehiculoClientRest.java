package org.lubrimax.msvc.ordenes.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-vehiculos", url = "${clients.vehiculos.url}")
public interface VehiculoClientRest {
    @GetMapping("/{id}")
    VehiculoRemoto detalle(@PathVariable Long id);
}
