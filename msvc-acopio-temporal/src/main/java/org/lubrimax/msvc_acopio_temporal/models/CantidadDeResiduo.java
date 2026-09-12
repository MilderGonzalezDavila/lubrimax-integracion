package org.lubrimax.msvc_acopio_temporal.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Objects;

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
        if (unidad == null || unidad.isBlank()) {
            throw new IllegalArgumentException("La unidad de la cantidad es obligatoria");
        }
        this.valor = valor;
        this.unidad = unidad.trim().toUpperCase();
    }

    public CantidadDeResiduo sumar(CantidadDeResiduo otra) {
        validarMismaUnidad(otra);
        return new CantidadDeResiduo(valor.add(otra.valor), unidad);
    }

    public void validarMismaUnidad(CantidadDeResiduo otra) {
        if (otra == null || !unidad.equals(otra.unidad)) {
            throw new IllegalArgumentException("Las cantidades de residuo deben usar la misma unidad");
        }
    }

    public BigDecimal getValor() { return valor; }
    public String getUnidad() { return unidad; }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) return true;
        if (!(otro instanceof CantidadDeResiduo cantidad)) return false;
        return valor.compareTo(cantidad.valor) == 0 && Objects.equals(unidad, cantidad.unidad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor.stripTrailingZeros(), unidad);
    }
}
