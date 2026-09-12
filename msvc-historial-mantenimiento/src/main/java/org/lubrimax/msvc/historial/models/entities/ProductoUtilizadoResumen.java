package org.lubrimax.msvc.historial.models.entities;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Embeddable
public class ProductoUtilizadoResumen {

    @NotBlank(message = "es obligatorio")
    private String codigo;

    @NotBlank(message = "es obligatoria")
    private String descripcion;

    @NotNull(message = "es obligatoria")
    @Positive(message = "debe ser mayor que cero")
    private Integer cantidad;

    public ProductoUtilizadoResumen() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
