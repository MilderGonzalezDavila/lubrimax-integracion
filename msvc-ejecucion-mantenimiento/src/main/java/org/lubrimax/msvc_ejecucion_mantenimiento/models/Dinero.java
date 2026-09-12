package org.lubrimax.msvc_ejecucion_mantenimiento.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class Dinero {

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false, length = 3)
    private String moneda;

    protected Dinero() {}

    public Dinero(BigDecimal monto, String moneda) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        if (moneda == null || moneda.isBlank())
            throw new IllegalArgumentException("La moneda es obligatoria");
        this.monto = monto;
        this.moneda = moneda.trim().toUpperCase();
    }

    public Dinero sumar(Dinero otro) {
        validarMoneda(otro);
        return new Dinero(monto.add(otro.monto), moneda);
    }

    public Dinero multiplicar(BigDecimal factor) {
        if (factor == null || factor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El factor no puede ser negativo");
        }
        return new Dinero(monto.multiply(factor), moneda);
    }

    private void validarMoneda(Dinero otro) {
        if (otro == null || !moneda.equals(otro.moneda)) {
            throw new IllegalArgumentException("No se pueden operar importes de monedas distintas");
        }
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public String getMoneda() {
        return moneda;
    }
}
