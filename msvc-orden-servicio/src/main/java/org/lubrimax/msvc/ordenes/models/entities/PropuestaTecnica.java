package org.lubrimax.msvc.ordenes.models.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "propuestas_tecnicas")
public class PropuestaTecnica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private EstadoPropuesta estado = EstadoPropuesta.BORRADOR;

    @Valid
    @NotEmpty(message = "debe contener al menos un concepto")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "conceptos_propuestos", joinColumns = @JoinColumn(name = "propuesta_id"))
    private List<ConceptoPropuesto> conceptos = new ArrayList<>();

    public PropuestaTecnica() {
    }

    public void presentar() {
        if (estado != EstadoPropuesta.BORRADOR && estado != EstadoPropuesta.MODIFICADA) {
            throw new IllegalArgumentException("Solo una propuesta en borrador o modificada puede presentarse");
        }
        estado = EstadoPropuesta.PRESENTADA;
    }

    public void aceptar() { estado = EstadoPropuesta.ACEPTADA; }
    public void rechazar() { estado = EstadoPropuesta.RECHAZADA; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public EstadoPropuesta getEstado() { return estado; }
    public void setEstado(EstadoPropuesta estado) { this.estado = estado; }
    public List<ConceptoPropuesto> getConceptos() { return conceptos; }
    public void setConceptos(List<ConceptoPropuesto> conceptos) { this.conceptos = conceptos; }
}
