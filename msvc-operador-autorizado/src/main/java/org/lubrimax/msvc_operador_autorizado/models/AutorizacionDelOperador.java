package org.lubrimax.msvc_operador_autorizado.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class AutorizacionDelOperador {

    @Column(name = "registro_eors", nullable = false, unique = true)
    private String registroEors;

    @Embedded
    private PeriodoDeVigencia periodoDeVigencia;

    protected AutorizacionDelOperador() {
    }

    public AutorizacionDelOperador(String registroEors, PeriodoDeVigencia periodoDeVigencia) {
        if (registroEors == null || registroEors.isBlank()) {
            throw new IllegalArgumentException("El registro EORS es obligatorio");
        }
        if (periodoDeVigencia == null) {
            throw new IllegalArgumentException("El periodo de vigencia es obligatorio");
        }
        this.registroEors = registroEors.trim();
        this.periodoDeVigencia = periodoDeVigencia;
    }

    public boolean estaVigenteAl(LocalDate fecha) {
        return periodoDeVigencia.estaVigenteAl(fecha);
    }

    public String getRegistroEors() {
        return registroEors;
    }

    public PeriodoDeVigencia getPeriodoDeVigencia() {
        return periodoDeVigencia;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) return true;
        if (!(otro instanceof AutorizacionDelOperador autorizacion)) return false;
        return Objects.equals(registroEors, autorizacion.registroEors)
                && Objects.equals(periodoDeVigencia, autorizacion.periodoDeVigencia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registroEors, periodoDeVigencia);
    }
}
