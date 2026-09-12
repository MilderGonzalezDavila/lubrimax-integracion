package org.lubrimax.msvc.ordenes.models.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Embeddable
public class ConceptoAutorizado {

    @NotNull(message = "es obligatorio")
    @Enumerated(EnumType.STRING)
    private TipoConcepto tipo;

    @NotNull(message = "es obligatoria")
    @Positive(message = "debe ser mayor que cero")
    private Long referenciaId;

    @NotNull(message = "es obligatoria")
    @Positive(message = "debe ser mayor que cero")
    private Integer cantidadAprobada;

    @NotNull(message = "es obligatorio")
    @DecimalMin(value = "0.00", message = "no puede ser negativo")
    private BigDecimal precioAcordado;

    public ConceptoAutorizado() {}

    public TipoConcepto getTipo() {
        return tipo;
    }

    public void setTipo(TipoConcepto tipo) {
        this.tipo = tipo;
    }

    public Long getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(Long referenciaId) {
        this.referenciaId = referenciaId;
    }

    public Integer getCantidadAprobada() {
        return cantidadAprobada;
    }

    public void setCantidadAprobada(Integer cantidadAprobada) {
        this.cantidadAprobada = cantidadAprobada;
    }

    public BigDecimal getPrecioAcordado() {
        return precioAcordado;
    }

    public void setPrecioAcordado(BigDecimal precioAcordado) {
        this.precioAcordado = precioAcordado;
    }
}
