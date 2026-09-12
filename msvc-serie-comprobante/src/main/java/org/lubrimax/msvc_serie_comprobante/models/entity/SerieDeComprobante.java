package org.lubrimax.msvc_serie_comprobante.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.lubrimax.msvc_serie_comprobante.models.TipoDeComprobante;

@Entity
@Table(
        name = "series_comprobante",
        uniqueConstraints = @UniqueConstraint(columnNames = {"codigo", "tipo"}))
public class SerieDeComprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version private int version;

    @NotBlank
    @Column(nullable = false)
    private String codigo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDeComprobante tipo;

    @Min(1)
    private long rangoInicio;

    @Min(1)
    private long rangoFin;

    private long correlativoActual;

    private boolean activa = true;

    public String siguienteNumero() {
        if (!activa) {
            throw new IllegalStateException("La serie esta inactiva");
        }
        long siguiente = correlativoActual == 0 ? rangoInicio : correlativoActual + 1;
        if (siguiente > rangoFin) {
            throw new IllegalStateException("La serie esta agotada");
        }
        correlativoActual = siguiente;
        return codigo + "-" + String.format("%08d", siguiente);
    }

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String v) {
        codigo = v;
    }

    public TipoDeComprobante getTipo() {
        return tipo;
    }

    public void setTipo(TipoDeComprobante v) {
        tipo = v;
    }

    public long getRangoInicio() {
        return rangoInicio;
    }

    public void setRangoInicio(long v) {
        rangoInicio = v;
    }

    public long getRangoFin() {
        return rangoFin;
    }

    public void setRangoFin(long v) {
        rangoFin = v;
    }

    public long getCorrelativoActual() {
        return correlativoActual;
    }

    public void setCorrelativoActual(long v) {
        correlativoActual = v;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean v) {
        activa = v;
    }
}
