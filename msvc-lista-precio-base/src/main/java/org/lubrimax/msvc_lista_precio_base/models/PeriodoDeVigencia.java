package org.lubrimax.msvc_lista_precio_base.models;

import jakarta.persistence.Embeddable;

import java.time.*;

@Embeddable
public class PeriodoDeVigencia {
    private LocalDateTime desde;
    private LocalDateTime hasta;

    public boolean estaVigente(LocalDateTime fecha) {
        return fecha != null
                && desde != null
                && !fecha.isBefore(desde)
                && (hasta == null || !fecha.isAfter(hasta));
    }

    public LocalDateTime getDesde() {
        return desde;
    }

    public void setDesde(LocalDateTime v) {
        desde = v;
    }

    public LocalDateTime getHasta() {
        return hasta;
    }

    public void setHasta(LocalDateTime v) {
        hasta = v;
    }
}
