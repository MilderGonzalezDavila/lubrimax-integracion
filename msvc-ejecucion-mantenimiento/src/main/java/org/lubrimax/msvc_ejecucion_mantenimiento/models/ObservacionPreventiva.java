package org.lubrimax.msvc_ejecucion_mantenimiento.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public class ObservacionPreventiva {

    @Column(name = "tipo_observacion", nullable = false)
    private String tipo;

    @Column(name = "descripcion_observacion", nullable = false, length = 500)
    private String descripcion;

    @Column(name = "momento_observacion", nullable = false)
    private LocalDateTime momento;

    protected ObservacionPreventiva() {
    }

    public ObservacionPreventiva(String tipo, String descripcion, LocalDateTime momento) {
        if (tipo == null || tipo.isBlank()) throw new IllegalArgumentException("El tipo de observacion es obligatorio");
        if (descripcion == null || descripcion.isBlank()) throw new IllegalArgumentException("La descripcion es obligatoria");
        this.tipo = tipo.trim();
        this.descripcion = descripcion.trim();
        this.momento = momento == null ? LocalDateTime.now() : momento;
    }

    public String getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
    public LocalDateTime getMomento() { return momento; }
}
