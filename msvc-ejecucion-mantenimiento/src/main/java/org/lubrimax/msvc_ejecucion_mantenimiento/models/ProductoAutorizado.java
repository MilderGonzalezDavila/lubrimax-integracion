package org.lubrimax.msvc_ejecucion_mantenimiento.models;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public class ProductoAutorizado {
    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Embedded
    @AttributeOverride(
            name = "monto",
            column = @Column(name = "precio_producto", nullable = false, precision = 12, scale = 2))
    @AttributeOverride(
            name = "moneda",
            column = @Column(name = "moneda_producto", nullable = false, length = 3))
    @AttributeOverride(
            name = "momento",
            column = @Column(name = "momento_precio_producto", nullable = false))
    private PrecioAplicado precio;

    protected ProductoAutorizado() {}

    public ProductoAutorizado(Long productoId, PrecioAplicado precio) {
        if (productoId == null || precio == null)
            throw new IllegalArgumentException("El producto y su precio son obligatorios");
        this.productoId = productoId;
        this.precio = precio;
    }

    public Long getProductoId() {
        return productoId;
    }

    public PrecioAplicado getPrecio() {
        return precio;
    }
}
