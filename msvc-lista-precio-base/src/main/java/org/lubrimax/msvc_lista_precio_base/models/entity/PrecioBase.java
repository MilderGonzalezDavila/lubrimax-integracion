package org.lubrimax.msvc_lista_precio_base.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.lubrimax.msvc_lista_precio_base.models.TipoDeReferencia;

import java.math.BigDecimal;

@Entity
@Table(
        name = "precios_base",
        uniqueConstraints =
                @UniqueConstraint(columnNames = {"lista_id", "tipo_referencia", "referencia_id"}))
public class PrecioBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_referencia", nullable = false)
    private TipoDeReferencia tipoReferencia;

    @NotNull
    @Column(name = "referencia_id", nullable = false)
    private Long referenciaId;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @NotBlank
    @Column(nullable = false, length = 3)
    private String moneda = "PEN";

    public Long getId() {
        return id;
    }

    public TipoDeReferencia getTipoReferencia() {
        return tipoReferencia;
    }

    public void setTipoReferencia(TipoDeReferencia tipoReferencia) {
        this.tipoReferencia = tipoReferencia;
    }

    public Long getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(Long referenciaId) {
        this.referenciaId = referenciaId;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
}
