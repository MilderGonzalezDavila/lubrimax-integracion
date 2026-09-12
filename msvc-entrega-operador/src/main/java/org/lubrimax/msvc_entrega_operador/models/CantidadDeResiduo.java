package org.lubrimax.msvc_entrega_operador.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class CantidadDeResiduo {

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal valor;

    @Column(nullable = false, length = 20)
    private String unidad;

    protected CantidadDeResiduo() {
    }

    public CantidadDeResiduo(BigDecimal valor, String unidad) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad de residuo debe ser mayor que cero");
        }
        if (unidad == null || unidad.isBlank()) throw new IllegalArgumentException("La unidad es obligatoria");
        this.valor = valor;
        this.unidad = unidad.trim().toUpperCase();
    }

    public BigDecimal getValor() { return valor; }
    public String getUnidad() { return unidad; }
}
