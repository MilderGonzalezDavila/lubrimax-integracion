package org.lubrimax.msvc_operador_autorizado.services;

import org.lubrimax.msvc_operador_autorizado.models.AutorizacionDelOperador;
import org.lubrimax.msvc_operador_autorizado.models.Ruc;
import org.lubrimax.msvc_operador_autorizado.models.Telefono;
import org.lubrimax.msvc_operador_autorizado.models.entity.OperadorAutorizado;

import java.time.LocalDate;
import java.util.List;

public interface OperadorAutorizadoService {
    OperadorAutorizado registrar(Ruc ruc, String razonSocial, Telefono telefono, AutorizacionDelOperador autorizacion);
    OperadorAutorizado buscarPorId(Long id);
    List<OperadorAutorizado> listar();
    OperadorAutorizado actualizarDatos(Long id, String razonSocial, Telefono telefono);
    OperadorAutorizado actualizarAutorizacion(Long id, AutorizacionDelOperador autorizacion);
    boolean consultarVigencia(Long id, LocalDate fecha);
    List<OperadorAutorizado> listarVigentes(LocalDate fecha);
}
