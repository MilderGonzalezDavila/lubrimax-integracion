package org.lubrimax.msvc_politicas.models.entity;
import jakarta.persistence.*;
import org.lubrimax.msvc_politicas.models.values.UmbralOperativo;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "politicas_negocio")
public class PoliticaDelNegocio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version; // Control de concurrencia optimista requerido por el diagrama[cite: 3]

    private String tipo;
    private double margen;
    private String periodoVigencia;

    @Embedded
    private UmbralOperativo umbrales;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "politica_id")
    private List<Promocion> promociones = new ArrayList<>();

    public void renovar() {
        this.version++;
    }

    public void cerrarVigencia() {
        this.periodoVigencia = "CERRADA";
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getMargen() {
        return margen;
    }

    public void setMargen(double margen) {
        this.margen = margen;
    }

    public String getPeriodoVigencia() {
        return periodoVigencia;
    }

    public void setPeriodoVigencia(String periodoVigencia) {
        this.periodoVigencia = periodoVigencia;
    }

    public List<Promocion> getPromociones() {
        return promociones;
    }

    public void setPromociones(List<Promocion> promociones) {
        this.promociones = promociones;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public UmbralOperativo getUmbrales() {
        return umbrales;
    }

    public void setUmbrales(UmbralOperativo umbrales) {
        this.umbrales = umbrales;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
