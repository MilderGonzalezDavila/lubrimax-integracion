package org.lubrimax.msvc_entrega_operador.models.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import org.lubrimax.msvc_entrega_operador.models.CantidadDeResiduo;
import org.lubrimax.msvc_entrega_operador.models.TipoDeResiduo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "lineas_entrega")
public class LineaDeEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDeResiduo tipoDeResiduo;

    @Embedded
    @AttributeOverride(
            name = "valor",
            column = @Column(name = "cantidad", nullable = false, precision = 12, scale = 3))
    @AttributeOverride(
            name = "unidad",
            column = @Column(name = "unidad", nullable = false, length = 20))
    private CantidadDeResiduo cantidad;

    @ElementCollection
    @CollectionTable(name = "lineas_entrega_residuos", joinColumns = @JoinColumn(name = "linea_id"))
    @Column(name = "residuo_id", nullable = false)
    private List<Long> residuosIds = new ArrayList<>();

    protected LineaDeEntrega() {}

    public LineaDeEntrega(
            TipoDeResiduo tipoDeResiduo, CantidadDeResiduo cantidad, List<Long> residuosIds) {
        if (tipoDeResiduo == null)
            throw new IllegalArgumentException("El tipo de residuo es obligatorio");
        if (cantidad == null) throw new IllegalArgumentException("La cantidad es obligatoria");
        if (residuosIds == null || residuosIds.isEmpty()) {
            throw new IllegalArgumentException("La linea debe incluir al menos un residuo");
        }
        if (residuosIds.stream().anyMatch(id -> id == null)
                || residuosIds.stream().distinct().count() != residuosIds.size()) {
            throw new IllegalArgumentException(
                    "Los residuos de la linea deben ser validos y no repetirse");
        }
        this.tipoDeResiduo = tipoDeResiduo;
        this.cantidad = cantidad;
        this.residuosIds.addAll(residuosIds);
    }

    public Long getId() {
        return id;
    }

    public TipoDeResiduo getTipoDeResiduo() {
        return tipoDeResiduo;
    }

    public CantidadDeResiduo getCantidad() {
        return cantidad;
    }

    public List<Long> getResiduosIds() {
        return Collections.unmodifiableList(residuosIds);
    }
}
