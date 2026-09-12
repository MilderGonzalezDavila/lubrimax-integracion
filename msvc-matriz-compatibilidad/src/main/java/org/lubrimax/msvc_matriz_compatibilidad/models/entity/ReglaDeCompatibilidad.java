package org.lubrimax.msvc_matriz_compatibilidad.models.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.lubrimax.msvc_matriz_compatibilidad.models.CriterioDeCompatibilidad;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reglas_compatibilidad")
public class ReglaDeCompatibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @Valid @Embedded private CriterioDeCompatibilidad criterio;

    @NotEmpty
    @ElementCollection
    @CollectionTable(name = "regla_productos", joinColumns = @JoinColumn(name = "regla_id"))
    @Column(name = "producto_id")
    private Set<Long> productosAdmitidos = new HashSet<>();

    public boolean aplicaA(String motor, Integer cc, String combustible, Integer anio) {
        return criterio.aplicaA(motor, cc, combustible, anio);
    }

    public Long getId() {
        return id;
    }

    public CriterioDeCompatibilidad getCriterio() {
        return criterio;
    }

    public void setCriterio(CriterioDeCompatibilidad v) {
        criterio = v;
    }

    public Set<Long> getProductosAdmitidos() {
        return productosAdmitidos;
    }

    public void setProductosAdmitidos(Set<Long> v) {
        productosAdmitidos = v;
    }
}
