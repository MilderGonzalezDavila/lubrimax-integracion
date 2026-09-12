package org.lubrimax.msvc.inventario.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.lubrimax.msvc.inventario.models.EstadoReserva;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas_temporales")
public class ReservaTemporal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "orden_id", nullable = false)
    private Long orden;

    @NotNull
    @Positive
    @Column(nullable = false)
    private BigDecimal cantidad;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReserva estado;

    @NotNull private LocalDateTime creada;

    @NotNull
    @Column(name = "expira_en")
    private LocalDateTime expiraEn;

    public ReservaTemporal() {}

    public ReservaTemporal(Long orden, BigDecimal cantidad, LocalDateTime expiraEn) {
        if (orden == null) {
            throw new IllegalArgumentException("La orden es obligatoria");
        }
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        this.orden = orden;
        this.cantidad = cantidad;
        this.estado = EstadoReserva.VIGENTE;
        this.creada = LocalDateTime.now();
        this.expiraEn = expiraEn;
    }

    public boolean haExpirado(LocalDateTime momento) {
        if (this.estado != EstadoReserva.VIGENTE) {
            return false;
        }
        if (this.expiraEn == null) {
            return false;
        }
        return !momento.isBefore(this.expiraEn);
    }

    public void confirmar() {
        if (this.estado != EstadoReserva.VIGENTE) {
            throw new IllegalStateException("Solo se puede confirmar una reserva vigente");
        }
        if (haExpirado(LocalDateTime.now())) {
            throw new IllegalStateException("No se puede confirmar una reserva expirada");
        }
        this.estado = EstadoReserva.CONFIRMADA;
    }

    public void liberar() {
        if (this.estado != EstadoReserva.VIGENTE && this.estado != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException(
                    "Solo se puede liberar una reserva vigente o confirmada");
        }
        this.estado = EstadoReserva.LIBERADA;
    }

    public void expirar() {
        if (this.estado != EstadoReserva.VIGENTE) {
            throw new IllegalStateException("Solo una reserva vigente puede expirar");
        }
        this.estado = EstadoReserva.EXPIRADA;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrden() {
        return orden;
    }

    public void setOrden(Long orden) {
        this.orden = orden;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public LocalDateTime getCreada() {
        return creada;
    }

    public LocalDateTime getExpiraEn() {
        return expiraEn;
    }
}
