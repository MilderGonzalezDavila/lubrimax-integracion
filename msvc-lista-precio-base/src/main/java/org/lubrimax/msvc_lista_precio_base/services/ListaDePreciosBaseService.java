package org.lubrimax.msvc_lista_precio_base.services;

import org.lubrimax.msvc_lista_precio_base.models.*;
import org.lubrimax.msvc_lista_precio_base.models.entity.*;

import java.util.*;

public interface ListaDePreciosBaseService {
    List<ListaDePreciosBase> listar();

    ListaDePreciosBase obtener(Long id);

    ListaDePreciosBase crear(ListaDePreciosBase l);

    ListaDePreciosBase agregarPrecio(Long id, PrecioBase p);

    ListaDePreciosBase activar(Long id);

    ListaDePreciosBase cerrar(Long id);

    PrecioBase precioVigente(TipoDeReferencia tipo, Long referenciaId);
}
