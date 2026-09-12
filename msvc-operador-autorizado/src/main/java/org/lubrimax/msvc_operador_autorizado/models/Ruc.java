package org.lubrimax.msvc_operador_autorizado.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Ruc {

    @Column(name = "ruc", nullable = false, unique = true, length = 11)
    private String numero;

    protected Ruc() {
    }

    public Ruc(String numero) {
        if (numero == null || !numero.matches("\\d{11}")) {
            throw new IllegalArgumentException("El RUC debe contener exactamente 11 digitos");
        }
        this.numero = numero;
    }

    public String getNumero() {
        return numero;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) return true;
        if (!(otro instanceof Ruc ruc)) return false;
        return Objects.equals(numero, ruc.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }
}
