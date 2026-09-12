package org.lubrimax.msvc_operador_autorizado.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Telefono {

    @Column(name = "telefono", nullable = false, length = 15)
    private String numero;

    protected Telefono() {}

    public Telefono(String numero) {
        if (numero == null || !numero.matches("[0-9+ -]{7,15}")) {
            throw new IllegalArgumentException("El telefono no tiene un formato valido");
        }
        this.numero = numero.trim();
    }

    public String getNumero() {
        return numero;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (!(otro instanceof Telefono telefono)) {
            return false;
        }
        return Objects.equals(numero, telefono.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }
}
