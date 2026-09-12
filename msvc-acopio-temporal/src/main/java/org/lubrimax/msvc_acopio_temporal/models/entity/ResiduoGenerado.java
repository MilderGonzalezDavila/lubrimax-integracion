package org.lubrimax.msvc_acopio_temporal.models.entity;

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
import jakarta.persistence.UniqueConstraint;
import org.lubrimax.msvc_acopio_temporal.models.CantidadDeResiduo;
import org.lubrimax.msvc_acopio_temporal.models.EstadoDelResiduo;
import org.lubrimax.msvc_acopio_temporal.models.TipoDeResiduo;

import java.time.LocalDateTime;

@Entity
@Table(name = "residuos_generados", uniqueConstraints =
        @UniqueConstraint(columnNames = {"orden_id", "declaracion_origen_id"}))
public class ResiduoGenerado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "orden_id", nullable = false)
    private Long ordenId;

    @Column(name = "declaracion_origen_id")
    private Long declaracionOrigenId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_residuo", nullable = false)
    private TipoDeResiduo tipo;

    @Embedded
    @AttributeOverride(name = "valor", column = @Column(name = "cantidad", nullable = false, precision = 12, scale = 3))
    @AttributeOverride(name = "unidad", column = @Column(name = "unidad", nullable = false, length = 20))
    private CantidadDeResiduo cantidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDelResiduo estado;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime fechaGeneracion;

    @Column(name = "entrega_id")
    private Long entregaId;

    protected ResiduoGenerado() {
    }

    public ResiduoGenerado(Long ordenId, Long declaracionOrigenId, TipoDeResiduo tipo,
                           CantidadDeResiduo cantidad, LocalDateTime fechaGeneracion) {
        if (ordenId == null) throw new IllegalArgumentException("La orden de origen es obligatoria");
        if (tipo == null) throw new IllegalArgumentException("El tipo de residuo es obligatorio");
        if (cantidad == null) throw new IllegalArgumentException("La cantidad de residuo es obligatoria");
        this.ordenId = ordenId;
        this.declaracionOrigenId = declaracionOrigenId;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.fechaGeneracion = fechaGeneracion == null ? LocalDateTime.now() : fechaGeneracion;
        this.estado = EstadoDelResiduo.GENERADO;
    }

    public void almacenar() {
        if (estado != EstadoDelResiduo.GENERADO) {
            throw new IllegalStateException("Solo un residuo GENERADO puede almacenarse");
        }
        estado = EstadoDelResiduo.ALMACENADO;
    }

    public void entregar(Long entregaId) {
        if (entregaId == null) throw new IllegalArgumentException("La entrega es obligatoria");
        if (estado == EstadoDelResiduo.ENTREGADO && entregaId.equals(this.entregaId)) return;
        if (estado != EstadoDelResiduo.ALMACENADO) {
            throw new IllegalStateException("Solo un residuo ALMACENADO puede entregarse");
        }
        estado = EstadoDelResiduo.ENTREGADO;
        this.entregaId = entregaId;
    }

    public Long getId() { return id; }
    public Long getOrdenId() { return ordenId; }
    public Long getDeclaracionOrigenId() { return declaracionOrigenId; }
    public TipoDeResiduo getTipo() { return tipo; }
    public CantidadDeResiduo getCantidad() { return cantidad; }
    public EstadoDelResiduo getEstado() { return estado; }
    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
    public Long getEntregaId() { return entregaId; }
}
