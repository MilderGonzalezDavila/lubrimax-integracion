package org.lubrimax.msvc_agregar_producto.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "presentaciones_producto")
public class PresentacionDeProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal contenido;

    @NotBlank
    @Column(nullable = false)
    private String unidad;

    @NotBlank
    @Column(nullable = false)
    private String envase;

    private boolean activa = true;

    public Long getId() {
        return id;
    }

    public BigDecimal getContenido() {
        return contenido;
    }

    public void setContenido(BigDecimal contenido) {
        this.contenido = contenido;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getEnvase() {
        return envase;
    }

    public void setEnvase(String envase) {
        this.envase = envase;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
