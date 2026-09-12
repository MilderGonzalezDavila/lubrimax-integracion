package org.lubrimax.msvc.ordenes.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-historial-mantenimiento", url = "${clients.historial.url}")
public interface HistorialClientRest {

    @GetMapping("/{vehiculoId}/resumen")
    ResumenHistorialRemoto resumen(@PathVariable Long vehiculoId);
}
