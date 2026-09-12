package org.lubrimax.msvc_ejecucion_mantenimiento.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class LiquidacionDeServicio {

    @Column(name = "subtotal_productos", precision = 12, scale = 2)
    private BigDecimal subtotalProductos;

    @Column(name = "subtotal_mano_obra", precision = 12, scale = 2)
    private BigDecimal subtotalManoDeObra;

    @Column(name = "total_liquidacion", precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "moneda_liquidacion", length = 3)
    private String moneda;

    protected LiquidacionDeServicio() {
    }

    public LiquidacionDeServicio(Dinero productos, Dinero manoDeObra) {
        Dinero totalCalculado = productos.sumar(manoDeObra);
        this.subtotalProductos = productos.getMonto();
        this.subtotalManoDeObra = manoDeObra.getMonto();
        this.total = totalCalculado.getMonto();
        this.moneda = totalCalculado.getMoneda();
    }

    public BigDecimal getSubtotalProductos() { return subtotalProductos; }
    public BigDecimal getSubtotalManoDeObra() { return subtotalManoDeObra; }
    public BigDecimal getTotal() { return total; }
    public String getMoneda() { return moneda; }
}
