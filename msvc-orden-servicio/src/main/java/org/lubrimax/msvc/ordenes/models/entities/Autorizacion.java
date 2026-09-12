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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autorizaciones")
public class Autorizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "es obligatoria")
    @Positive(message = "debe ser mayor que cero")
    @Column(nullable = false)
    private Long propuestaId;

    @NotNull(message = "es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private AlcanceAutorizacion alcance;

    @NotNull(message = "es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private MedioAutorizacion medio;

    @Column(nullable = false)
    private LocalDateTime momento;

    @Valid
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "conceptos_autorizados",
            joinColumns = @JoinColumn(name = "autorizacion_id"))
    private List<ConceptoAutorizado> conceptos = new ArrayList<>();

    public Autorizacion() {}

    public void registrarMomento() {
        momento = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPropuestaId() {
        return propuestaId;
    }

    public void setPropuestaId(Long propuestaId) {
        this.propuestaId = propuestaId;
    }

    public AlcanceAutorizacion getAlcance() {
        return alcance;
    }

    public void setAlcance(AlcanceAutorizacion alcance) {
        this.alcance = alcance;
    }

    public MedioAutorizacion getMedio() {
        return medio;
    }

    public void setMedio(MedioAutorizacion medio) {
        this.medio = medio;
    }

    public LocalDateTime getMomento() {
        return momento;
    }

    public void setMomento(LocalDateTime momento) {
        this.momento = momento;
    }

    public List<ConceptoAutorizado> getConceptos() {
        return conceptos;
    }

    public void setConceptos(List<ConceptoAutorizado> conceptos) {
        this.conceptos = conceptos;
    }
}
