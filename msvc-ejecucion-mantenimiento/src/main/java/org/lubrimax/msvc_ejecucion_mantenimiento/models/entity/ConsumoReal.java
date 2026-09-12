package org.lubrimax.msvc_ejecucion_mantenimiento.models.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.lubrimax.msvc_ejecucion_mantenimiento.models.CantidadDeProducto;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.Dinero;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.PrecioAplicado;

@Entity
@Table(name = "consumos_reales")
public class ConsumoReal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Embedded
    @AttributeOverride(
            name = "valor",
            column = @Column(name = "cantidad", nullable = false, precision = 12, scale = 3))
    @AttributeOverride(
            name = "unidad",
            column = @Column(name = "unidad", nullable = false, length = 20))
    private CantidadDeProducto cantidad;

    @Embedded
    @AttributeOverride(
            name = "monto",
            column = @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2))
    @AttributeOverride(
            name = "moneda",
            column = @Column(name = "moneda", nullable = false, length = 3))
    @AttributeOverride(
            name = "momento",
            column = @Column(name = "momento_precio", nullable = false))
    private PrecioAplicado precioAplicado;

    protected ConsumoReal() {}

    public ConsumoReal(
            Long productoId, CantidadDeProducto cantidad, PrecioAplicado precioAplicado) {
        if (productoId == null || cantidad == null || precioAplicado == null) {
            throw new IllegalArgumentException(
                    "El producto, la cantidad y el precio son obligatorios");
        }
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioAplicado = precioAplicado;
    }

    public Dinero subtotal() {
        return precioAplicado.comoDinero().multiplicar(cantidad.getValor());
    }

    public Long getId() {
        return id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public CantidadDeProducto getCantidad() {
        return cantidad;
    }

    public PrecioAplicado getPrecioAplicado() {
        return precioAplicado;
    }
}
