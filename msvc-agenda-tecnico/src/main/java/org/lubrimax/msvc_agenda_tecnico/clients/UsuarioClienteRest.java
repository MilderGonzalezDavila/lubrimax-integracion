package org.lubrimax.msvc_agenda_tecnico.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-administracion", url = "${services.administracion.url}")
public interface UsuarioClienteRest {

    @GetMapping("/api/usuarios/{id}")
    UsuarioResponse buscarUsuario(@PathVariable("id") Long id);

    record UsuarioResponse(Long id, String rol, boolean activo) {
        public boolean esTecnicoActivo() {
            return activo && rol != null && rol.equalsIgnoreCase("TECNICO");
        }
    }
}
