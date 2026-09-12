package org.lubrimax.msvc_entrega_operador.services;

import org.lubrimax.msvc_entrega_operador.models.CantidadDeResiduo;
import org.lubrimax.msvc_entrega_operador.models.ManifiestoDeResiduos;
import org.lubrimax.msvc_entrega_operador.models.TipoDeResiduo;
import org.lubrimax.msvc_entrega_operador.models.entity.EntregaAOperador;

import java.time.LocalDate;
import java.util.List;

public interface EntregaAOperadorService {
    EntregaAOperador programar(Long operadorId, Long usuarioId, LocalDate fechaEntrega);
    EntregaAOperador obtener(Long entregaId);
    List<EntregaAOperador> listar();
    EntregaAOperador agregarLinea(Long entregaId, TipoDeResiduo tipo, CantidadDeResiduo cantidad, List<Long> residuosIds);
    EntregaAOperador eliminarLinea(Long entregaId, Long lineaId);
    EntregaAOperador registrarManifiesto(Long entregaId, ManifiestoDeResiduos manifiesto);
    EntregaAOperador ejecutar(Long entregaId);
    EntregaAOperador conformar(Long entregaId);
}
