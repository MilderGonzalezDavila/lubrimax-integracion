package org.lubrimax.msvc.ordenes.models.entities;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

@Embeddable
public class MantenimientoSolicitado {

    @NotBlank(message = "es obligatorio")
    private String tipoServicio;

    @NotBlank(message = "es obligatoria")
    private String descripcion;

    public MantenimientoSolicitado() {}

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
