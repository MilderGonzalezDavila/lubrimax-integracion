package org.lubrimax.msvc_ejecucion_mantenimiento.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public class ProgramacionDeProximoServicio {

    @Column(name = "proxima_fecha")
    private LocalDate fechaSugerida;

    @Column(name = "proximo_kilometraje")
    private Long kilometrajeSugerido;

    protected ProgramacionDeProximoServicio() {}

    public ProgramacionDeProximoServicio(LocalDate fechaSugerida, Long kilometrajeSugerido) {
        if (fechaSugerida == null && kilometrajeSugerido == null) {
            throw new IllegalArgumentException(
                    "Debe indicar una fecha o un kilometraje para el proximo servicio");
        }
        if (kilometrajeSugerido != null && kilometrajeSugerido < 0) {
            throw new IllegalArgumentException("El kilometraje sugerido no puede ser negativo");
        }
        this.fechaSugerida = fechaSugerida;
        this.kilometrajeSugerido = kilometrajeSugerido;
    }

    public LocalDate getFechaSugerida() {
        return fechaSugerida;
    }

    public Long getKilometrajeSugerido() {
        return kilometrajeSugerido;
    }
}
