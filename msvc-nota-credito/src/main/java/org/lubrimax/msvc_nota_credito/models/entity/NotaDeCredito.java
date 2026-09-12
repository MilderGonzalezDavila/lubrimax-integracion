package org.lubrimax.msvc_nota_credito.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.lubrimax.msvc_nota_credito.models.MotivoDeNotaDeCredito;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "notas_credito")
public class NotaDeCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version private int version;

    @NotNull private Long comprobanteId;

    @NotNull private Long serieId;

    @Column(nullable = false, unique = true)
    private String numero;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MotivoDeNotaDeCredito motivo;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal monto;

    @NotBlank private String moneda = "PEN";

    private LocalDateTime emitidaEn;

    public void emitir(String numero, BigDecimal totalOriginal) {
        if (monto.compareTo(totalOriginal) > 0) {
            throw new IllegalArgumentException("La nota no puede exceder el total del comprobante");
        }
        this.numero = numero;
        emitidaEn = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public Long getComprobanteId() {
        return comprobanteId;
    }

    public void setComprobanteId(Long v) {
        comprobanteId = v;
    }

    public Long getSerieId() {
        return serieId;
    }

    public void setSerieId(Long v) {
        serieId = v;
    }

    public String getNumero() {
        return numero;
    }

    public MotivoDeNotaDeCredito getMotivo() {
        return motivo;
    }

    public void setMotivo(MotivoDeNotaDeCredito v) {
        motivo = v;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal v) {
        monto = v;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String v) {
        moneda = v;
    }

    public LocalDateTime getEmitidaEn() {
        return emitidaEn;
    }
}
