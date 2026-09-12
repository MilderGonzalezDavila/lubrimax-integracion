package org.lubrimax.msvc.inventario.models.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import org.lubrimax.msvc.inventario.models.EstadoReserva;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(
        name = "existencias",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"producto_id", "presentacion_id"})})
public class ExistenciaDeProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(name = "presentacion_id", nullable = false)
    private Long presentacionId;

    @Embedded private SaldoDeAlmacen saldo;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "existencia_id")
    private List<ReservaTemporal> reservas = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "existencia_id")
    private List<MovimientoDeStock> movimientos = new ArrayList<>();

    public ExistenciaDeProducto() {}

    public ExistenciaDeProducto(Long productoId, Long presentacionId, SaldoDeAlmacen saldo) {
        this.productoId = productoId;
        this.presentacionId = presentacionId;
        this.saldo = saldo;
    }

    // REGLAS DE NEGOCIO (MÉTODOS DEL AGREGADO)

    public BigDecimal disponible() {
        return this.saldo.disponible();
    }

    public void consumir(Long orden, BigDecimal cantidad) {
        if (orden == null) {
            throw new IllegalArgumentException("La orden es obligatoria");
        }

        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad a consumir debe ser mayor que cero");
        }

        Optional<ReservaTemporal> reservaConfirmada =
                this.reservas.stream()
                        .filter(
                                r ->
                                        r.getOrden().equals(orden)
                                                && r.getEstado() == EstadoReserva.CONFIRMADA)
                        .findFirst();

        if (reservaConfirmada.isPresent()) {
            ReservaTemporal reserva = reservaConfirmada.get();

            BigDecimal reservadoDeEstaOrden = reserva.getCantidad();

            BigDecimal reservadoDeOtrasOrdenes =
                    this.saldo.getReservado().subtract(reservadoDeEstaOrden);

            BigDecimal maximoConsumible = this.saldo.getFisico().subtract(reservadoDeOtrasOrdenes);

            if (cantidad.compareTo(maximoConsumible) > 0) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para consumir sin afectar reservas de otras órdenes");
            }

            this.saldo.restarFisico(cantidad);
            this.saldo.restarReservado(reservadoDeEstaOrden);
            reserva.liberar();
            return;
        }

        // Si no existe reserva confirmada,
        // el consumo solo puede usar stock disponible.
        if (!this.saldo.admiteConsumo(cantidad)) {
            throw new IllegalArgumentException(
                    "Stock disponible insuficiente o cantidad inválida para consumir");
        }

        this.saldo.restarFisico(cantidad);
    }

    public void reservar(Long orden, BigDecimal cantidad, LocalDateTime expiraEn) {

        if (orden == null) {
            throw new IllegalArgumentException("La orden es obligatoria");
        }

        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad a reservar debe ser mayor que cero");
        }

        if (expiraEn == null || !expiraEn.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de expiración debe ser futura");
        }

        boolean yaExisteReservaActiva =
                this.reservas.stream()
                        .anyMatch(
                                r ->
                                        r.getOrden().equals(orden)
                                                && (r.getEstado() == EstadoReserva.VIGENTE
                                                        || r.getEstado()
                                                                == EstadoReserva.CONFIRMADA));

        if (yaExisteReservaActiva) {
            throw new IllegalArgumentException(
                    "Ya existe una reserva activa para la orden: " + orden);
        }

        if (cantidad.compareTo(disponible()) > 0) {
            throw new IllegalArgumentException("No existe stock suficiente para reservar");
        }

        ReservaTemporal reserva = new ReservaTemporal(orden, cantidad, expiraEn);

        this.reservas.add(reserva);
        this.saldo.sumarReservado(cantidad);
    }

    public void confirmarReserva(Long orden) {
        if (orden == null) {
            throw new IllegalArgumentException("La orden es obligatoria");
        }

        expirarReservas(LocalDateTime.now());

        ReservaTemporal reserva =
                this.reservas.stream()
                        .filter(
                                r ->
                                        r.getOrden().equals(orden)
                                                && r.getEstado() == EstadoReserva.VIGENTE)
                        .findFirst()
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "No existe una reserva vigente para la orden: "
                                                        + orden));

        reserva.confirmar();
    }

    public void liberarReserva(Long orden) {
        ReservaTemporal reserva =
                this.reservas.stream()
                        .filter(
                                r ->
                                        r.getOrden().equals(orden)
                                                && r.getEstado() == EstadoReserva.VIGENTE)
                        .findFirst()
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "No existe una reserva vigente para la orden: "
                                                        + orden));

        this.saldo.restarReservado(reserva.getCantidad());
        reserva.liberar();
    }

    public int expirarReservas(LocalDateTime momento) {
        int expiradas = 0;

        for (ReservaTemporal reserva : this.reservas) {
            if (reserva.haExpirado(momento)) {
                this.saldo.restarReservado(reserva.getCantidad());
                reserva.expirar();
                expiradas++;
            }
        }

        return expiradas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Long getPresentacionId() {
        return presentacionId;
    }

    public void setPresentacionId(Long presentacionId) {
        this.presentacionId = presentacionId;
    }

    public SaldoDeAlmacen getSaldo() {
        return saldo;
    }

    public void setSaldo(SaldoDeAlmacen saldo) {
        this.saldo = saldo;
    }

    public List<ReservaTemporal> getReservas() {
        return reservas;
    }

    public void setReservas(List<ReservaTemporal> reservas) {
        this.reservas = reservas;
    }

    public List<MovimientoDeStock> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<MovimientoDeStock> movimientos) {
        this.movimientos = movimientos;
    }
}
