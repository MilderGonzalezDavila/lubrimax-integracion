package org.lubrimax.msvc_politicas.models.values;

import jakarta.persistence.Embeddable;

@Embeddable
public class UmbralOperativo {

    private int stockMinimo;
    private int diasParaBajaRotacion;
    private int diasSinRotacion;
    private double limiteDeAcopio;

    public UmbralOperativo() {}

    public UmbralOperativo(
            int stockMinimo, int diasParaBajaRotacion, int diasSinRotacion, double limiteDeAcopio) {
        this.stockMinimo = stockMinimo;
        this.diasParaBajaRotacion = diasParaBajaRotacion;
        this.diasSinRotacion = diasSinRotacion;
        this.limiteDeAcopio = limiteDeAcopio;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public int getDiasParaBajaRotacion() {
        return diasParaBajaRotacion;
    }

    public int getDiasSinRotacion() {
        return diasSinRotacion;
    }

    public double getLimiteDeAcopio() {
        return limiteDeAcopio;
    }
}
