package org.lubrimax.msvc.abastecimiento.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.lubrimax.msvc.abastecimiento.models.EstadoDeObservacion;
import org.lubrimax.msvc.abastecimiento.models.EstadoDeRecepcion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "recepciones_mercaderia")
public class RecepcionDeMercaderia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recepcion_id")
    private Long id; // Corresponde a RecepcionId id

    @Version
    private int version;

    @NotNull
    @Column(name = "proveedor_id", nullable = false)
    private Long proveedorId;

    @NotNull
    @Column(name = "documento_recepcion", nullable = false)
    private String documento;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDeRecepcion estado;

    @NotNull
    @Column(name = "fecha_recepcion", nullable = false)
    private LocalDate fechaRecepcion;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "recepcion_id")
    private List<ObservacionDeRecepcion> observaciones = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "recepcion_id")
    private List<LineaDeRecepcion> lineas = new ArrayList<>();

    public RecepcionDeMercaderia() {
    }

    public RecepcionDeMercaderia(Long proveedorId, String documento) {
        if (proveedorId == null) {
            throw new IllegalArgumentException("El proveedor es obligatorio");
        }

        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("El documento de recepción es obligatorio");
        }
        this.proveedorId = proveedorId;
        this.documento = documento;
        this.estado = EstadoDeRecepcion.RECIBIDO;
        this.fechaRecepcion = LocalDate.now();
    }

    public void verificar() {
        if (this.estado == EstadoDeRecepcion.CERRADO) {
            throw new IllegalStateException("No se puede verificar una recepción ya cerrada");
        }
    }

    public void conformar() {
        if (this.estado == EstadoDeRecepcion.CERRADO) {
            throw new IllegalStateException("La recepción ya está cerrada");
        }

        boolean tienePendientes = this.observaciones.stream()
                .anyMatch(obs -> obs.getEstado() != EstadoDeObservacion.RESUELTO);

        if (tienePendientes) {
            throw new IllegalStateException("No se puede dar conformidad si existen observaciones pendientes");
        }

        this.estado = EstadoDeRecepcion.CONFORME;
    }

    public void observar() {
        if (this.estado == EstadoDeRecepcion.CERRADO) {
            throw new IllegalStateException("No se puede observar una recepción cerrada");
        }
        this.estado = EstadoDeRecepcion.OBSERVADO;
    }

    public void cerrar() {
        if (this.estado != EstadoDeRecepcion.CONFORME && this.estado != EstadoDeRecepcion.OBSERVADO) {
            throw new IllegalStateException("Solo se puede cerrar una recepción que esté CONFORME u OBSERVADA");
        }
        this.estado = EstadoDeRecepcion.CERRADO;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }

    public Long getProveedorId() { return proveedorId; }
    public void setProveedorId(Long proveedorId) { this.proveedorId = proveedorId; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public EstadoDeRecepcion getEstado() { return estado; }
    public void setEstado(EstadoDeRecepcion estado) { this.estado = estado; }

    public LocalDate getFechaRecepcion() { return fechaRecepcion; }
    public void setFechaRecepcion(LocalDate fechaRecepcion) { this.fechaRecepcion = fechaRecepcion; }

    public List<ObservacionDeRecepcion> getObservaciones() { return observaciones; }
    public void setObservaciones(List<ObservacionDeRecepcion> observaciones) { this.observaciones = observaciones; }

    public List<LineaDeRecepcion> getLineas() { return lineas; }
    public void setLineas(List<LineaDeRecepcion> lineas) { this.lineas = lineas; }
}