package org.lubrimax.msvc_acopio_temporal.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
public class CapacidadDeAcopio {

    @Column(name = "capacidad_maxima", nullable = false, precision = 12, scale = 3)
    private BigDecimal valorMaximo;

    @Column(name = "capacidad_unidad", nullable = false, length = 20)
    private String unidad;

    protected CapacidadDeAcopio() {}

    public CapacidadDeAcopio(BigDecimal valorMaximo, String unidad) {
        if (valorMaximo == null || valorMaximo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero");
        }
        if (unidad == null || unidad.isBlank()) {
            throw new IllegalArgumentException("La unidad de capacidad es obligatoria");
        }
        this.valorMaximo = valorMaximo;
        this.unidad = unidad.trim().toUpperCase();
    }

    public boolean admite(BigDecimal acumulado, CantidadDeResiduo nuevaCantidad) {
        validarUnidad(nuevaCantidad);
        return acumulado.add(nuevaCantidad.getValor()).compareTo(valorMaximo) <= 0;
    }

    public BigDecimal disponible(BigDecimal acumulado) {
        return valorMaximo.subtract(acumulado).max(BigDecimal.ZERO);
    }

    public CondicionDeAcopio condicion(BigDecimal acumulado) {
        if (acumulado.compareTo(valorMaximo) >= 0) return CondicionDeAcopio.AL_LIMITE;
        BigDecimal porcentaje = acumulado.divide(valorMaximo, 4, RoundingMode.HALF_UP);
        return porcentaje.compareTo(new BigDecimal("0.80")) >= 0
                ? CondicionDeAcopio.PROXIMO_AL_LIMITE
                : CondicionDeAcopio.NORMAL;
    }

    private void validarUnidad(CantidadDeResiduo cantidad) {
        if (!unidad.equals(cantidad.getUnidad())) {
            throw new IllegalArgumentException(
                    "La unidad del residuo no coincide con la unidad del acopio");
        }
    }

    public BigDecimal getValorMaximo() {
        return valorMaximo;
    }

    public String getUnidad() {
        return unidad;
    }
}
