package org.lubrimax.msvc_nota_credito.services;

import org.lubrimax.msvc_nota_credito.models.entity.NotaDeCredito;

import java.util.List;

public interface NotaDeCreditoService {

    List<NotaDeCredito> listar();

    NotaDeCredito obtener(Long id);

    NotaDeCredito emitir(NotaDeCredito nota);
}
