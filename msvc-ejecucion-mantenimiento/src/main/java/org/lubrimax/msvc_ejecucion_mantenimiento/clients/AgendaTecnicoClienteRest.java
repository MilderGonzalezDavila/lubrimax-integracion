package org.lubrimax.msvc_ejecucion_mantenimiento.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-agenda-tecnico", url = "${services.agenda-tecnico.url}")
public interface AgendaTecnicoClienteRest {

    @GetMapping("/api/agendas/{usuarioId}/asignaciones/orden/{ordenId}/valida")
    AsignacionResponse validarAsignacion(@PathVariable("usuarioId") Long usuarioId,
                                         @PathVariable("ordenId") Long ordenId);

    record AsignacionResponse(boolean valida) {}
}
