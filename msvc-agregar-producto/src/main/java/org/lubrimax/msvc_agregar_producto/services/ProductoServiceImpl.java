package org.lubrimax.msvc_agregar_producto.services;

import org.lubrimax.msvc_agregar_producto.models.CategoriaDeProducto;
import org.lubrimax.msvc_agregar_producto.models.entity.PresentacionDeProducto;
import org.lubrimax.msvc_agregar_producto.models.entity.Producto;
import org.lubrimax.msvc_agregar_producto.repositories.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repository;

    public ProductoServiceImpl(ProductoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listar() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Producto obtener(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
    }

    @Override
    @Transactional
    public Producto crear(Producto producto) {
        if (repository.existsByCodigo(producto.getCodigo())) {
            throw new IllegalArgumentException("El codigo ya existe");
        }
        validarCategoria(producto);
        return repository.save(producto);
    }

    @Override
    @Transactional
    public Producto actualizar(Long id, Producto datos) {
        Producto actual = obtener(id);
        if (!actual.getCodigo().equals(datos.getCodigo())
                && repository.existsByCodigo(datos.getCodigo())) {
            throw new IllegalArgumentException("El codigo ya existe");
        }
        validarCategoria(datos);
        actual.setCodigo(datos.getCodigo());
        actual.setNombre(datos.getNombre());
        actual.setMarca(datos.getMarca());
        actual.setCategoria(datos.getCategoria());
        actual.setTipoAceite(datos.getTipoAceite());
        actual.setViscosidad(datos.getViscosidad());
        actual.setEspecificacion(datos.getEspecificacion());
        actual.setTipoFiltro(datos.getTipoFiltro());
        actual.setIntervalo(datos.getIntervalo());
        actual.setEstado(datos.getEstado());
        return repository.save(actual);
    }

    @Override
    @Transactional
    public Producto agregarPresentacion(Long id, PresentacionDeProducto presentacion) {
        Producto producto = obtener(id);
        producto.getPresentaciones().add(presentacion);
        return repository.save(producto);
    }

    @Override
    @Transactional
    public Producto desactivar(Long id) {
        Producto producto = obtener(id);
        producto.desactivar();
        return repository.save(producto);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Producto no encontrado");
        }
        repository.deleteById(id);
    }

    private void validarCategoria(Producto producto) {
        if (producto.getCategoria() == null) {
            throw new IllegalArgumentException("La categoria es obligatoria");
        }
        if (producto.getCategoria() == CategoriaDeProducto.ACEITE
                && producto.getTipoAceite() == null) {
            throw new IllegalArgumentException("Un aceite requiere tipo de aceite");
        }
        if (producto.getCategoria() == CategoriaDeProducto.FILTRO
                && producto.getTipoFiltro() == null) {
            throw new IllegalArgumentException("Un filtro requiere tipo de filtro");
        }
    }
}
