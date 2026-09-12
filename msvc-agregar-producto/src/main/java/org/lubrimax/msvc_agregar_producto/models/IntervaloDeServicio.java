package org.lubrimax.msvc_agregar_producto.models;

import jakarta.persistence.Embeddable;

@Embeddable
public class IntervaloDeServicio {

    private Integer kilometros;
    private Integer meses;

    public Integer getKilometros() {
        return kilometros;
    }

    public void setKilometros(Integer kilometros) {
        this.kilometros = kilometros;
    }

    public Integer getMeses() {
        return meses;
    }

    public void setMeses(Integer meses) {
        this.meses = meses;
    }
}
