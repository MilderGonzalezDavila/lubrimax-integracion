package org.lubrimax.msvc.abastecimiento.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "msvc-inventario", url = "http://localhost:8007/api/inventario")
public interface InventarioClienteRest {

    @PostMapping("/habilitar")
    void habilitarStock(
            @RequestParam Long productoId,
            @RequestParam Long presentacionId,
            @RequestParam BigDecimal cantidad,
            @RequestParam Long recepcionId);
}
