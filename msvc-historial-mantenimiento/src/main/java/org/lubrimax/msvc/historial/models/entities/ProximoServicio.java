package org.lubrimax.msvc.historial.models.entities;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public class ProximoServicio {

    private LocalDate fechaSugerida;
    private Long kilometrajeSugerido;

    public ProximoServicio() {}

    public boolean estaDefinido() {
        return fechaSugerida != null || kilometrajeSugerido != null;
    }

    public LocalDate getFechaSugerida() {
        return fechaSugerida;
    }

    public void setFechaSugerida(LocalDate fechaSugerida) {
        this.fechaSugerida = fechaSugerida;
    }

    public Long getKilometrajeSugerido() {
        return kilometrajeSugerido;
    }

    public void setKilometrajeSugerido(Long kilometrajeSugerido) {
        this.kilometrajeSugerido = kilometrajeSugerido;
    }
}
