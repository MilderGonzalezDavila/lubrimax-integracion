package org.lubrimax.msvc_ejecucion_mantenimiento.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.Duration;
import java.time.LocalDateTime;

@Embeddable
public class PeriodoDeTrabajo {

    @Column(name = "inicio_tarea")
    private LocalDateTime inicio;

    @Column(name = "fin_tarea")
    private LocalDateTime fin;

    protected PeriodoDeTrabajo() {}

    public PeriodoDeTrabajo(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio == null)
            throw new IllegalArgumentException("El inicio del trabajo es obligatorio");
        if (fin != null && !fin.isAfter(inicio)) {
            throw new IllegalArgumentException("El fin debe ser posterior al inicio");
        }
        this.inicio = inicio;
        this.fin = fin;
    }

    public PeriodoDeTrabajo finalizar(LocalDateTime fin) {
        return new PeriodoDeTrabajo(inicio, fin);
    }

    public Duration duracion() {
        if (fin == null) throw new IllegalStateException("El periodo aun no ha finalizado");
        return Duration.between(inicio, fin);
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }
}
