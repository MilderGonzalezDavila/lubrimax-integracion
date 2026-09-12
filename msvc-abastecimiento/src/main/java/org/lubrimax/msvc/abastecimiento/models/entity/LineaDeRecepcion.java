package org.lubrimax.msvc.abastecimiento.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import org.lubrimax.msvc.abastecimiento.models.ResultadoDeVerificacion;

import java.math.BigDecimal;

@Entity
@Table(name = "lineas_recepcion")
public class LineaDeRecepcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "linea_id")
    private Long id;

    @NotNull
    @Column(name = "presentacion_id", nullable = false)
    private Long presentacion;

    @NotNull
    @Positive
    @Column(name = "cantidad_producto", nullable = false)
    private BigDecimal cantidadDeProducto;

    @NotNull
    @PositiveOrZero
    @Column(name = "costo_entrada", nullable = false)
    private BigDecimal costoDeEntrada;

    @Column(nullable = false)
    private String lote;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResultadoDeVerificacion verificacion;

    @NotNull
    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    public LineaDeRecepcion() {}

    public LineaDeRecepcion(
            Long presentacion,
            BigDecimal cantidadDeProducto,
            BigDecimal costoDeEntrada,
            String lote,
            ResultadoDeVerificacion verificacion,
            Long productoId) {
        if (presentacion == null) {
            throw new IllegalArgumentException("La presentación es obligatoria");
        }
        if (lote == null || lote.isBlank()) {
            throw new IllegalArgumentException("El lote es obligatorio");
        }

        if (cantidadDeProducto == null || cantidadDeProducto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad del producto debe ser mayor que 0");
        }

        if (costoDeEntrada == null || costoDeEntrada.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El costo de entrada no debe ser negativo");
        }

        if (verificacion == null) {
            throw new IllegalArgumentException("El resultado de verificación es obligatorio");
        }

        if (productoId == null) {
            throw new IllegalArgumentException("El producto es obligatorio");
        }
        this.presentacion = presentacion;
        this.cantidadDeProducto = cantidadDeProducto;
        this.costoDeEntrada = costoDeEntrada;
        this.lote = lote;
        this.verificacion = verificacion;
        this.productoId = productoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(Long presentacion) {
        this.presentacion = presentacion;
    }

    public BigDecimal getCantidadDeProducto() {
        return cantidadDeProducto;
    }

    public void setCantidadDeProducto(BigDecimal cantidadDeProducto) {
        this.cantidadDeProducto = cantidadDeProducto;
    }

    public BigDecimal getCostoDeEntrada() {
        return costoDeEntrada;
    }

    public void setCostoDeEntrada(BigDecimal costoDeEntrada) {
        this.costoDeEntrada = costoDeEntrada;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public ResultadoDeVerificacion getVerificacion() {
        return verificacion;
    }

    public void setVerificacion(ResultadoDeVerificacion verificacion) {
        this.verificacion = verificacion;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }
}
