package org.lubrimax.msvc_ejecucion_mantenimiento.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Embeddable
public class PrecioAplicado {

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false, length = 3)
    private String moneda;

    @Column(nullable = false)
    private LocalDateTime momento;

    protected PrecioAplicado() {}

    public PrecioAplicado(BigDecimal monto, String moneda, LocalDateTime momento) {
        Dinero dinero = new Dinero(monto, moneda);
        if (momento == null)
            throw new IllegalArgumentException("El momento del precio es obligatorio");
        this.monto = dinero.getMonto();
        this.moneda = dinero.getMoneda();
        this.momento = momento;
    }

    public Dinero comoDinero() {
        return new Dinero(monto, moneda);
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public LocalDateTime getMomento() {
        return momento;
    }
}
