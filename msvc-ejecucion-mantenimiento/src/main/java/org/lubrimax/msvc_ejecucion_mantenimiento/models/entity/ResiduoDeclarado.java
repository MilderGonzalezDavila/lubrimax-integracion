package org.lubrimax.msvc_ejecucion_mantenimiento.models.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.TipoDeResiduoGenerado;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.VolumenDeclarado;

import java.time.LocalDateTime;

@Entity
@Table(name = "residuos_declarados")
public class ResiduoDeclarado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tarea_id", nullable = false)
    private Long tareaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_residuo", nullable = false)
    private TipoDeResiduoGenerado tipo;

    @Embedded
    @AttributeOverride(name = "valor", column = @Column(name = "volumen", nullable = false, precision = 12, scale = 3))
    @AttributeOverride(name = "unidad", column = @Column(name = "unidad", nullable = false, length = 20))
    private VolumenDeclarado volumen;

    @Column(name = "momento_declaracion", nullable = false)
    private LocalDateTime momentoDeclaracion;

    protected ResiduoDeclarado() {
    }

    public ResiduoDeclarado(Long tareaId, TipoDeResiduoGenerado tipo, VolumenDeclarado volumen) {
        if (tareaId == null || tipo == null || volumen == null) {
            throw new IllegalArgumentException("La tarea, el tipo y el volumen del residuo son obligatorios");
        }
        this.tareaId = tareaId;
        this.tipo = tipo;
        this.volumen = volumen;
        this.momentoDeclaracion = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getTareaId() { return tareaId; }
    public TipoDeResiduoGenerado getTipo() { return tipo; }
    public VolumenDeclarado getVolumen() { return volumen; }
    public LocalDateTime getMomentoDeclaracion() { return momentoDeclaracion; }
}
