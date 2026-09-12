package org.lubrimax.msvc.abastecimiento.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import org.lubrimax.msvc.abastecimiento.models.CondicionDelInsumo;
import org.lubrimax.msvc.abastecimiento.models.EstadoDeObservacion;

@Entity
@Table(name = "observaciones_recepcion")
public class ObservacionDeRecepcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "observacion_id")
    private Long id;

    @NotNull
    @Column(name = "linea_id", nullable = false)
    private Long linea;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CondicionDelInsumo condicion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDeObservacion estado;

    public ObservacionDeRecepcion() {}

    public ObservacionDeRecepcion(Long linea, CondicionDelInsumo condicion) {
        this.linea = linea;
        this.condicion = condicion;
        this.estado = EstadoDeObservacion.PENDIENTE;
    }

    public void resolver() {
        this.estado = EstadoDeObservacion.RESUELTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLinea() {
        return linea;
    }

    public void setLinea(Long linea) {
        this.linea = linea;
    }

    public CondicionDelInsumo getCondicion() {
        return condicion;
    }

    public void setCondicion(CondicionDelInsumo condicion) {
        this.condicion = condicion;
    }

    public EstadoDeObservacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoDeObservacion estado) {
        this.estado = estado;
    }
}
