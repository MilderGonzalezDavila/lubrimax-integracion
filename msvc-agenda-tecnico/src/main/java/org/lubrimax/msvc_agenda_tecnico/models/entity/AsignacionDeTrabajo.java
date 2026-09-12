package org.lubrimax.msvc_agenda_tecnico.models.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.lubrimax.msvc_agenda_tecnico.models.EstadoDeAsignacion;
import org.lubrimax.msvc_agenda_tecnico.models.PeriodoDeTrabajo;

@Entity
@Table(name = "asignaciones_trabajo")
public class AsignacionDeTrabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ordenId;

    @Embedded
    private PeriodoDeTrabajo periodo;

    @Enumerated(EnumType.STRING)
    private EstadoDeAsignacion estado;

    protected AsignacionDeTrabajo() {
    }

    public AsignacionDeTrabajo(Long ordenId, PeriodoDeTrabajo periodo) {
        if (ordenId == null) throw new IllegalArgumentException("La orden es obligatoria");
        if (periodo == null) throw new IllegalArgumentException("El periodo de trabajo es obligatorio");
        this.ordenId = ordenId;
        this.periodo = periodo;
        this.estado = EstadoDeAsignacion.ASIGNADA;
    }

    public void iniciar() {
        if (estado == EstadoDeAsignacion.LIBERADA) {
            throw new IllegalStateException("Una asignacion LIBERADA no puede volver a iniciarse");
        }
        if (estado != EstadoDeAsignacion.ASIGNADA) {
            throw new IllegalStateException("Solo una asignacion ASIGNADA puede iniciarse");
        }
        estado = EstadoDeAsignacion.EN_CURSO;
    }

    public void liberar() {
        if (estado == EstadoDeAsignacion.LIBERADA) return;
        estado = EstadoDeAsignacion.LIBERADA;
    }

    public boolean ocupaElPeriodo() {
        return estado == EstadoDeAsignacion.ASIGNADA || estado == EstadoDeAsignacion.EN_CURSO;
    }

    public Long getId() { return id; }
    public Long getOrdenId() { return ordenId; }
    public PeriodoDeTrabajo getPeriodo() { return periodo; }
    public EstadoDeAsignacion getEstado() { return estado; }
}
