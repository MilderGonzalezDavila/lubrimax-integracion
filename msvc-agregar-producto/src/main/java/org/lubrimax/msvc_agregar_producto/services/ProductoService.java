package org.lubrimax.msvc_agregar_producto.services;

import org.lubrimax.msvc_agregar_producto.models.entity.PresentacionDeProducto;
import org.lubrimax.msvc_agregar_producto.models.entity.Producto;

import java.util.List;

public interface ProductoService {

    List<Producto> listar();

    Producto obtener(Long id);

    Producto crear(Producto producto);

    Producto actualizar(Long id, Producto producto);

    Producto agregarPresentacion(Long id, PresentacionDeProducto presentacion);

    Producto desactivar(Long id);

    void eliminar(Long id);
}
