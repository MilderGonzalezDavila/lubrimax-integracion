package org.lubrimax.msvc_ejecucion_mantenimiento.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "msvc-orden-servicio", url = "${services.orden-servicio.url}")
public interface OrdenServicioClienteRest {

    @GetMapping("/api/ordenes/{ordenId}/ejecucion")
    OrdenAutorizadaResponse obtenerParaEjecucion(@PathVariable("ordenId") Long ordenId);

    record PrecioResponse(BigDecimal monto, String moneda, LocalDateTime momento) {}
    record ServicioAutorizadoResponse(Long servicioId, PrecioResponse precio) {}
    record ProductoAutorizadoResponse(Long productoId, PrecioResponse precio) {}
    record OrdenAutorizadaResponse(Long ordenId, String estado, boolean emitida,
                                    List<ServicioAutorizadoResponse> servicios,
                                    List<ProductoAutorizadoResponse> productos) {
        public boolean autorizadaYEmitida() {
            return emitida && estado != null
                    && (estado.equalsIgnoreCase("EMITIDA") || estado.equalsIgnoreCase("AUTORIZADA"));
        }
    }
}
