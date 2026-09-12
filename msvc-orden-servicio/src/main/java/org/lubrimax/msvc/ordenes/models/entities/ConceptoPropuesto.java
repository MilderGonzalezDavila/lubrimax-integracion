package org.lubrimax.msvc.ordenes.models.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Embeddable
public class ConceptoPropuesto {

    @NotNull(message = "es obligatorio")
    @Enumerated(EnumType.STRING)
    private TipoConcepto tipo;

    @NotNull(message = "es obligatoria")
    @Positive(message = "debe ser mayor que cero")
    private Long referenciaId;

    @NotNull(message = "es obligatoria")
    @Positive(message = "debe ser mayor que cero")
    private Integer cantidadEstimada;

    @NotNull(message = "es obligatorio")
    @DecimalMin(value = "0.00", message = "no puede ser negativo")
    private BigDecimal precioReferencial;

    @NotBlank(message = "es obligatoria")
    private String justificacion;

    public ConceptoPropuesto() {
    }

    public TipoConcepto getTipo() { return tipo; }
    public void setTipo(TipoConcepto tipo) { this.tipo = tipo; }
    public Long getReferenciaId() { return referenciaId; }
    public void setReferenciaId(Long referenciaId) { this.referenciaId = referenciaId; }
    public Integer getCantidadEstimada() { return cantidadEstimada; }
    public void setCantidadEstimada(Integer cantidadEstimada) { this.cantidadEstimada = cantidadEstimada; }
    public BigDecimal getPrecioReferencial() { return precioReferencial; }
    public void setPrecioReferencial(BigDecimal precioReferencial) { this.precioReferencial = precioReferencial; }
    public String getJustificacion() { return justificacion; }
    public void setJustificacion(String justificacion) { this.justificacion = justificacion; }
}
