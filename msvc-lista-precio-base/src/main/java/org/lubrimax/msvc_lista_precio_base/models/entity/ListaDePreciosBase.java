package org.lubrimax.msvc_lista_precio_base.models.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.lubrimax.msvc_lista_precio_base.models.PeriodoDeVigencia;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "listas_precios_base")
public class ListaDePreciosBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version private int version;

    @NotNull @Valid @Embedded private PeriodoDeVigencia vigencia;

    private boolean vigente;

    @Valid
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "lista_id", nullable = false)
    private List<PrecioBase> precios = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public PeriodoDeVigencia getVigencia() {
        return vigencia;
    }

    public void setVigencia(PeriodoDeVigencia vigencia) {
        this.vigencia = vigencia;
    }

    public boolean isVigente() {
        return vigente;
    }

    public void setVigente(boolean vigente) {
        this.vigente = vigente;
    }

    public List<PrecioBase> getPrecios() {
        return precios;
    }

    public void setPrecios(List<PrecioBase> precios) {
        this.precios = precios == null ? new ArrayList<>() : precios;
    }

    public void activar() {
        vigente = true;
    }

    public void cerrar() {
        vigente = false;
    }
}
