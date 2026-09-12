package org.lubrimax.msvc.abastecimiento.clients;

import org.lubrimax.msvc.abastecimiento.models.Proveedor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-proveedor", url = "http://localhost:8005/api/proveedores")
public interface ProveedorClienteRest {

    @GetMapping("/{id}")
    Proveedor buscarProveedorPorId(@PathVariable("id") Long id);
}
