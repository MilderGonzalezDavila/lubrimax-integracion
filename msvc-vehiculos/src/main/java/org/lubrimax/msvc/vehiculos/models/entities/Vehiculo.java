package org.lubrimax.msvc.vehiculos.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Locale;

@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "es obligatoria")
    @Pattern(regexp = "[A-Za-z0-9-]{6,10}", message = "no tiene un formato válido")
    @Column(nullable = false, unique = true, updatable = false, length = 10)
    private String placa;

    @Valid
    @NotNull(message = "es obligatoria")
    @Embedded
    private FichaTecnica fichaTecnica;

    @NotNull(message = "es obligatorio")
    @Positive(message = "debe ser mayor que cero")
    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @NotNull(message = "es obligatorio")
    @PositiveOrZero(message = "no puede ser negativo")
    @Column(nullable = false)
    private Long kilometrajeActual;

    @NotNull(message = "es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private EstadoVehiculo estado = EstadoVehiculo.ACTIVO;

    public Vehiculo() {
    }

    public void actualizarDatos(FichaTecnica fichaTecnica, Long clienteId, Long kilometrajeActual, EstadoVehiculo estado) {
        this.fichaTecnica = fichaTecnica;
        this.clienteId = clienteId;
        this.kilometrajeActual = kilometrajeActual;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa == null ? null : placa.trim().toUpperCase(Locale.ROOT);
    }

    public FichaTecnica getFichaTecnica() {
        return fichaTecnica;
    }

    public void setFichaTecnica(FichaTecnica fichaTecnica) {
        this.fichaTecnica = fichaTecnica;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getKilometrajeActual() {
        return kilometrajeActual;
    }

    public void setKilometrajeActual(Long kilometrajeActual) {
        this.kilometrajeActual = kilometrajeActual;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }

    public void setEstado(EstadoVehiculo estado) {
        this.estado = estado;
    }
}
