package org.lubrimax.msvc_agenda_tecnico.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class PeriodoDeTrabajo {

    @Column(name = "inicio", nullable = false)
    private LocalDateTime inicio;

    @Column(name = "fin", nullable = false)
    private LocalDateTime fin;

    protected PeriodoDeTrabajo() {}

    public PeriodoDeTrabajo(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio == null || fin == null) {
            throw new IllegalArgumentException("El inicio y el fin del periodo son obligatorios");
        }
        if (!fin.isAfter(inicio)) {
            throw new IllegalArgumentException("El fin del periodo debe ser posterior al inicio");
        }
        this.inicio = inicio;
        this.fin = fin;
    }

    public Duration duracion() {
        return Duration.between(inicio, fin);
    }

    public boolean seSolapaCon(PeriodoDeTrabajo otro) {
        if (otro == null)
            throw new IllegalArgumentException("El periodo a comparar es obligatorio");
        return inicio.isBefore(otro.fin) && otro.inicio.isBefore(fin);
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) return true;
        if (!(otro instanceof PeriodoDeTrabajo periodo)) return false;
        return Objects.equals(inicio, periodo.inicio) && Objects.equals(fin, periodo.fin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inicio, fin);
    }
}
