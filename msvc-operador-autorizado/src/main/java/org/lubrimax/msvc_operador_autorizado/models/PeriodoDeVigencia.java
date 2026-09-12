package org.lubrimax.msvc_operador_autorizado.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class PeriodoDeVigencia {

    @Column(name = "vigencia_desde", nullable = false)
    private LocalDate desde;

    @Column(name = "vigencia_hasta", nullable = false)
    private LocalDate hasta;

    protected PeriodoDeVigencia() {
    }

    public PeriodoDeVigencia(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            throw new IllegalArgumentException("El periodo de vigencia es obligatorio");
        }
        if (hasta.isBefore(desde)) {
            throw new IllegalArgumentException("La fecha final no puede ser anterior a la fecha inicial");
        }
        this.desde = desde;
        this.hasta = hasta;
    }

    public boolean estaVigenteAl(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha de consulta es obligatoria");
        }
        return !fecha.isBefore(desde) && !fecha.isAfter(hasta);
    }

    public LocalDate getDesde() {
        return desde;
    }

    public LocalDate getHasta() {
        return hasta;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) return true;
        if (!(otro instanceof PeriodoDeVigencia periodo)) return false;
        return Objects.equals(desde, periodo.desde) && Objects.equals(hasta, periodo.hasta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(desde, hasta);
    }
}
