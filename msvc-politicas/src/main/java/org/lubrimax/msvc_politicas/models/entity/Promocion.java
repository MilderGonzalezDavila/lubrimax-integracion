package org.lubrimax.msvc_politicas.models.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "promociones")
public class Promocion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productoId;
    private double porcentaje;

    private LocalDate vigenciaInicio;
    private LocalDate vigenciaFin;

    public boolean solaparConOtra(Promocion otraPromocion) {
        return (this.vigenciaInicio.isBefore(otraPromocion.getVigenciaFin())
                && this.vigenciaFin.isAfter(otraPromocion.getVigenciaInicio()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public LocalDate getVigenciaFin() {
        return vigenciaFin;
    }

    public void setVigenciaFin(LocalDate vigenciaFin) {
        this.vigenciaFin = vigenciaFin;
    }

    public LocalDate getVigenciaInicio() {
        return vigenciaInicio;
    }

    public void setVigenciaInicio(LocalDate vigenciaInicio) {
        this.vigenciaInicio = vigenciaInicio;
    }
}
