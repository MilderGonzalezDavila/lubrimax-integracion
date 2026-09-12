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
import org.lubrimax.msvc_ejecucion_mantenimiento.models.EstadoDeTarea;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.PeriodoDeTrabajo;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.PrecioAplicado;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.TipoDeTarea;

import java.time.LocalDateTime;

@Entity
@Table(name = "tareas_ejecutadas")
public class TareaEjecutada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "servicio_id", nullable = false)
    private Long servicioId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_tarea", nullable = false)
    private TipoDeTarea tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDeTarea estado;

    @Embedded
    private PeriodoDeTrabajo periodo;

    @Embedded
    @AttributeOverride(name = "monto", column = @Column(name = "precio_mano_obra", nullable = false, precision = 12, scale = 2))
    @AttributeOverride(name = "moneda", column = @Column(name = "moneda_mano_obra", nullable = false, length = 3))
    @AttributeOverride(name = "momento", column = @Column(name = "momento_precio_mano_obra", nullable = false))
    private PrecioAplicado precioAplicado;

    protected TareaEjecutada() {
    }

    public TareaEjecutada(Long servicioId, TipoDeTarea tipo, PrecioAplicado precioAplicado) {
        if (servicioId == null) throw new IllegalArgumentException("El servicio es obligatorio");
        if (tipo == null) throw new IllegalArgumentException("El tipo de tarea es obligatorio");
        if (precioAplicado == null) throw new IllegalArgumentException("El precio autorizado es obligatorio");
        this.servicioId = servicioId;
        this.tipo = tipo;
        this.precioAplicado = precioAplicado;
        this.estado = EstadoDeTarea.PENDIENTE;
    }

    public void iniciar() {
        if (estado != EstadoDeTarea.PENDIENTE) throw new IllegalStateException("Solo una tarea PENDIENTE puede iniciarse");
        estado = EstadoDeTarea.EN_CURSO;
        periodo = new PeriodoDeTrabajo(LocalDateTime.now(), null);
    }

    public void completar() {
        if (estado != EstadoDeTarea.EN_CURSO) throw new IllegalStateException("Solo una tarea EN_CURSO puede completarse");
        LocalDateTime fin = LocalDateTime.now();
        if (!fin.isAfter(periodo.getInicio())) {
            fin = periodo.getInicio().plusNanos(1);
        }
        periodo = periodo.finalizar(fin);
        estado = EstadoDeTarea.COMPLETADA;
    }

    public void marcarNoRealizada() {
        if (estado == EstadoDeTarea.COMPLETADA) throw new IllegalStateException("Una tarea COMPLETADA no puede marcarse como no realizada");
        if (estado == EstadoDeTarea.NO_REALIZADA) return;
        if (estado == EstadoDeTarea.EN_CURSO) {
            LocalDateTime fin = LocalDateTime.now();
            if (!fin.isAfter(periodo.getInicio())) {
                fin = periodo.getInicio().plusNanos(1);
            }
            periodo = periodo.finalizar(fin);
        }
        estado = EstadoDeTarea.NO_REALIZADA;
    }

    public Long getId() { return id; }
    public Long getServicioId() { return servicioId; }
    public TipoDeTarea getTipo() { return tipo; }
    public EstadoDeTarea getEstado() { return estado; }
    public PeriodoDeTrabajo getPeriodo() { return periodo; }
    public PrecioAplicado getPrecioAplicado() { return precioAplicado; }
}
