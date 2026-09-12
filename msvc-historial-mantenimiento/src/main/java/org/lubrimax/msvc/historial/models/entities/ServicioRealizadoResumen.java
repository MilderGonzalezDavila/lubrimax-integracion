package org.lubrimax.msvc.historial.models.entities;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

@Embeddable
public class ServicioRealizadoResumen {

    @NotBlank(message = "es obligatorio")
    private String tipoServicio;

    @NotBlank(message = "es obligatoria")
    private String descripcion;

    public ServicioRealizadoResumen() {
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
