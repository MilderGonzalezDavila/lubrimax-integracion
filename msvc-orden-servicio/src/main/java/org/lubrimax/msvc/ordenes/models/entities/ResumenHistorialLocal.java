package org.lubrimax.msvc.ordenes.models.entities;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public class ResumenHistorialLocal {

    private LocalDate ultimaFecha;
    private Long ultimoKilometraje;
    private LocalDate proximaFechaSugerida;
    private Long proximoKilometrajeSugerido;

    public ResumenHistorialLocal() {
    }

    public ResumenHistorialLocal(LocalDate ultimaFecha, Long ultimoKilometraje,
                                 LocalDate proximaFechaSugerida, Long proximoKilometrajeSugerido) {
        this.ultimaFecha = ultimaFecha;
        this.ultimoKilometraje = ultimoKilometraje;
        this.proximaFechaSugerida = proximaFechaSugerida;
        this.proximoKilometrajeSugerido = proximoKilometrajeSugerido;
    }

    public LocalDate getUltimaFecha() { return ultimaFecha; }
    public void setUltimaFecha(LocalDate ultimaFecha) { this.ultimaFecha = ultimaFecha; }
    public Long getUltimoKilometraje() { return ultimoKilometraje; }
    public void setUltimoKilometraje(Long ultimoKilometraje) { this.ultimoKilometraje = ultimoKilometraje; }
    public LocalDate getProximaFechaSugerida() { return proximaFechaSugerida; }
    public void setProximaFechaSugerida(LocalDate proximaFechaSugerida) { this.proximaFechaSugerida = proximaFechaSugerida; }
    public Long getProximoKilometrajeSugerido() { return proximoKilometrajeSugerido; }
    public void setProximoKilometrajeSugerido(Long proximoKilometrajeSugerido) { this.proximoKilometrajeSugerido = proximoKilometrajeSugerido; }
}
