package org.lubrimax.msvc.ordenes.clients;

import java.time.LocalDate;

public class ProximoServicioRemoto {

    private LocalDate fechaSugerida;
    private Long kilometrajeSugerido;

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
