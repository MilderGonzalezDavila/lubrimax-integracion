package org.lubrimax.msvc_ejecucion_mantenimiento.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Kilometraje {
    @Column(name = "kilometraje", nullable = false)
    private Long valor;

    protected Kilometraje() {}

    public Kilometraje(Long valor) {
        if (valor == null || valor < 0) throw new IllegalArgumentException("El kilometraje no puede ser negativo");
        this.valor = valor;
    }

    public Long getValor() { return valor; }
}
