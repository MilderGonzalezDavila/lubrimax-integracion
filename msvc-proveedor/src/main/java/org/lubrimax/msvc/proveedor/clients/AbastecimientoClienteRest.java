package org.lubrimax.msvc.proveedor.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "msvc-abastecimiento", url = "http://localhost:8006/api/abastecimiento")
public interface AbastecimientoClienteRest {

    @GetMapping("/existe-por-proveedor")
    boolean existePorProveedorId(@RequestParam("proveedorId") Long proveedorId);
}
