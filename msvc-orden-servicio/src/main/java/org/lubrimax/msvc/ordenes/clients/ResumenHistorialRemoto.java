package org.lubrimax.msvc.ordenes.clients;

import java.time.LocalDate;

public class ResumenHistorialRemoto {

    private LocalDate ultimaFecha;
    private Long ultimoKilometraje;
    private ProximoServicioRemoto proximoServicio;

    public LocalDate getUltimaFecha() {
        return ultimaFecha;
    }

    public void setUltimaFecha(LocalDate ultimaFecha) {
        this.ultimaFecha = ultimaFecha;
    }

    public Long getUltimoKilometraje() {
        return ultimoKilometraje;
    }

    public void setUltimoKilometraje(Long ultimoKilometraje) {
        this.ultimoKilometraje = ultimoKilometraje;
    }

    public ProximoServicioRemoto getProximoServicio() {
        return proximoServicio;
    }

    public void setProximoServicio(ProximoServicioRemoto proximoServicio) {
        this.proximoServicio = proximoServicio;
    }
}
