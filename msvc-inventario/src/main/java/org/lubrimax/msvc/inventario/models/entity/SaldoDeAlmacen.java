package org.lubrimax.msvc.inventario.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class SaldoDeAlmacen {

    @Column(name = "stock_fisico", nullable = false)
    private BigDecimal fisico;

    @Column(name = "stock_reservado", nullable = false)
    private BigDecimal reservado;

    public SaldoDeAlmacen() {}

    public SaldoDeAlmacen(BigDecimal fisico, BigDecimal reservado) {
        this.fisico = fisico;
        this.reservado = reservado;
    }

    public BigDecimal disponible() {
        return this.fisico.subtract(this.reservado);
    }

    public boolean admiteConsumo(BigDecimal cantidad) {
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad a consumir debe ser mayor que cero");
        }
        if (cantidad.compareTo(this.fisico) > 0) {
            throw new IllegalArgumentException("No existe stock físico suficiente");
        }
        return cantidad.compareTo(disponible()) <= 0;
    }

    public void restarFisico(BigDecimal cantidad) {
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad a consumir debe ser mayor que cero");
        }
        if (cantidad.compareTo(this.fisico) > 0) {
            throw new IllegalArgumentException("No existe stock físico suficiente");
        }
        this.fisico = this.fisico.subtract(cantidad);
    }

    public void sumarReservado(BigDecimal cantidad) {
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad a reservar debe ser mayor que cero");
        }
        this.reservado = this.reservado.add(cantidad);
    }

    public void restarReservado(BigDecimal cantidad) {
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad a liberar debe ser mayor que cero");
        }
        if (cantidad.compareTo(this.reservado) > 0) {
            throw new IllegalArgumentException(
                    "No se puede liberar una cantidad mayor al stock reservado");
        }
        this.reservado = this.reservado.subtract(cantidad);
    }

    public BigDecimal getFisico() {
        return fisico;
    }

    public BigDecimal getReservado() {
        return reservado;
    }
}
