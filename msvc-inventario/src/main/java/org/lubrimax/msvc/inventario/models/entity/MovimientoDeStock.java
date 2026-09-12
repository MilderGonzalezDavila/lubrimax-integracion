package org.lubrimax.msvc.inventario.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.lubrimax.msvc.inventario.models.MotivoDeAjuste;
import org.lubrimax.msvc.inventario.models.TipoMovimiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos_stock")
public class MovimientoDeStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipo;

    @Column(name = "tipo_origen")
    private String origen; // Cambiado a 'origen' según el diagrama (OrigenDeMovimiento)

    @NotNull
    @Positive
    @Column(nullable = false)
    private BigDecimal cantidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "motivo_ajuste")
    private MotivoDeAjuste motivo; // ¡Agregado tal cual pide tu diagrama!

    @NotNull
    @Column(nullable = false)
    private LocalDateTime momento; // Cambiado a 'momento' y tipo DateTime (LocalDateTime) según diagrama

    @Column(name = "referencia_origen")
    private Long referenciaOrigen;

    public MovimientoDeStock() {
    }


    public MovimientoDeStock(TipoMovimiento tipo, String origen, BigDecimal cantidad, MotivoDeAjuste motivo, Long referenciaOrigen) {
        this.tipo = tipo;
        this.origen = origen;
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.referenciaOrigen = referenciaOrigen;
        this.momento = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }

    public MotivoDeAjuste getMotivo() { return motivo; }
    public void setMotivo(MotivoDeAjuste motivo) { this.motivo = motivo; }

    public LocalDateTime getMomento() { return momento; }
    public void setMomento(LocalDateTime momento) { this.momento = momento; }

    public Long getReferenciaOrigen() { return referenciaOrigen; }
    public void setReferenciaOrigen(Long referenciaOrigen) { this.referenciaOrigen = referenciaOrigen; }
}