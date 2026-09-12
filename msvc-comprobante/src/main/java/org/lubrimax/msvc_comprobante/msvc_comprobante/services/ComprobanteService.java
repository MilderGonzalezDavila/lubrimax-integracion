package org.lubrimax.msvc_comprobante.msvc_comprobante.services;

import org.lubrimax.msvc_comprobante.msvc_comprobante.models.entity.Comprobante;
import org.lubrimax.msvc_comprobante.msvc_comprobante.models.entity.Pago;

import java.util.List;

public interface ComprobanteService {

    List<Comprobante> listar();

    Comprobante obtener(Long id);

    Comprobante emitir(Comprobante comprobante);

    Comprobante registrarPago(Long id, Pago pago);

    Comprobante anular(Long id);
}
