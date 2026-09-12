package org.lubrimax.msvc.ordenes.models.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordenes_servicio")
public class OrdenServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "es obligatorio")
    @Positive(message = "debe ser mayor que cero")
    @Column(nullable = false)
    private Long clienteId;

    @NotNull(message = "es obligatorio")
    @Positive(message = "debe ser mayor que cero")
    @Column(nullable = false)
    private Long vehiculoId;

    @NotNull(message = "es obligatorio")
    @PositiveOrZero(message = "no puede ser negativo")
    @Column(nullable = false)
    private Long kilometrajeCapturado;

    @Valid
    @NotNull(message = "es obligatorio")
    @Embedded
    private MantenimientoSolicitado mantenimientoSolicitado;

    @Embedded
    private ResumenHistorialLocal resumenHistorial;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private EstadoOrden estado = EstadoOrden.RECIBIDA;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "orden_id", nullable = false)
    private List<PropuestaTecnica> propuestas = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "orden_id", nullable = false)
    private List<Autorizacion> autorizaciones = new ArrayList<>();

    public OrdenServicio() {
    }

    public void agregarPropuesta(PropuestaTecnica propuesta) {
        asegurarNoEmitida();
        propuestas.add(propuesta);
        estado = EstadoOrden.EN_PROPUESTA;
    }

    public void presentarPropuesta(Long propuestaId) {
        asegurarNoEmitida();
        PropuestaTecnica propuesta = obtenerPropuesta(propuestaId);
        propuesta.presentar();
        estado = EstadoOrden.EN_PROPUESTA;
    }

    public void registrarAutorizacion(Autorizacion autorizacion) {
        asegurarNoEmitida();
        PropuestaTecnica propuesta = obtenerPropuesta(autorizacion.getPropuestaId());
        if (propuesta.getEstado() != EstadoPropuesta.PRESENTADA) {
            throw new IllegalArgumentException("La autorización requiere una propuesta presentada");
        }

        validarConceptosAutorizados(propuesta, autorizacion);
        autorizacion.registrarMomento();
        autorizaciones.add(autorizacion);

        if (autorizacion.getAlcance() == AlcanceAutorizacion.RECHAZO) {
            propuesta.rechazar();
            estado = EstadoOrden.RECHAZADA;
            return;
        }

        propuesta.aceptar();
        estado = autorizacion.getAlcance() == AlcanceAutorizacion.TOTAL
                ? EstadoOrden.AUTORIZADA
                : EstadoOrden.PARCIALMENTE_AUTORIZADA;
    }

    public void emitir() {
        if (estado != EstadoOrden.AUTORIZADA && estado != EstadoOrden.PARCIALMENTE_AUTORIZADA) {
            throw new IllegalArgumentException("La orden no se emite sin autorización registrada");
        }
        estado = EstadoOrden.EMITIDA;
    }

    private PropuestaTecnica obtenerPropuesta(Long propuestaId) {
        return propuestas.stream()
                .filter(propuesta -> propuesta.getId().equals(propuestaId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("La propuesta no pertenece a la orden"));
    }

    private void validarConceptosAutorizados(PropuestaTecnica propuesta, Autorizacion autorizacion) {
        if (autorizacion.getAlcance() == AlcanceAutorizacion.RECHAZO) {
            if (!autorizacion.getConceptos().isEmpty()) {
                throw new IllegalArgumentException("Una autorización rechazada no contiene conceptos");
            }
            return;
        }
        if (autorizacion.getConceptos().isEmpty()) {
            throw new IllegalArgumentException("La autorización debe contener conceptos aceptados");
        }

        for (ConceptoAutorizado autorizado : autorizacion.getConceptos()) {
            ConceptoPropuesto propuesto = propuesta.getConceptos().stream()
                    .filter(concepto -> concepto.getTipo() == autorizado.getTipo()
                            && concepto.getReferenciaId().equals(autorizado.getReferenciaId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Todo concepto autorizado debe provenir de la propuesta"));
            if (autorizado.getCantidadAprobada() > propuesto.getCantidadEstimada()) {
                throw new IllegalArgumentException("La cantidad autorizada no puede superar la cantidad propuesta");
            }
        }

        if (autorizacion.getAlcance() == AlcanceAutorizacion.TOTAL
                && autorizacion.getConceptos().size() != propuesta.getConceptos().size()) {
            throw new IllegalArgumentException("La autorización total debe incluir todos los conceptos propuestos");
        }
    }

    private void asegurarNoEmitida() {
        if (estado == EstadoOrden.EMITIDA) {
            throw new IllegalArgumentException("Una orden emitida es inmutable");
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public Long getVehiculoId() { return vehiculoId; }
    public void setVehiculoId(Long vehiculoId) { this.vehiculoId = vehiculoId; }
    public Long getKilometrajeCapturado() { return kilometrajeCapturado; }
    public void setKilometrajeCapturado(Long kilometrajeCapturado) { this.kilometrajeCapturado = kilometrajeCapturado; }
    public MantenimientoSolicitado getMantenimientoSolicitado() { return mantenimientoSolicitado; }
    public void setMantenimientoSolicitado(MantenimientoSolicitado mantenimientoSolicitado) { this.mantenimientoSolicitado = mantenimientoSolicitado; }
    public ResumenHistorialLocal getResumenHistorial() { return resumenHistorial; }
    public void setResumenHistorial(ResumenHistorialLocal resumenHistorial) { this.resumenHistorial = resumenHistorial; }
    public EstadoOrden getEstado() { return estado; }
    public void setEstado(EstadoOrden estado) { this.estado = estado; }
    public List<PropuestaTecnica> getPropuestas() { return propuestas; }
    public void setPropuestas(List<PropuestaTecnica> propuestas) { this.propuestas = propuestas; }
    public List<Autorizacion> getAutorizaciones() { return autorizaciones; }
    public void setAutorizaciones(List<Autorizacion> autorizaciones) { this.autorizaciones = autorizaciones; }
}
