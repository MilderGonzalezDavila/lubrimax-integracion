package org.lubrimax.msvc.historial.models.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "registros_mantenimiento")
public class RegistroMantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "es obligatoria")
    @PastOrPresent(message = "no puede ser futura")
    @Column(nullable = false)
    private LocalDate fechaAtencion;

    @NotNull(message = "es obligatorio")
    @PositiveOrZero(message = "no puede ser negativo")
    @Column(nullable = false)
    private Long kilometraje;

    @NotNull(message = "es obligatorio")
    @Positive(message = "debe ser mayor que cero")
    @Column(nullable = false)
    private Long ordenId;

    @NotNull(message = "es obligatorio")
    @Positive(message = "debe ser mayor que cero")
    @Column(nullable = false)
    private Long tecnicoId;

    @NotNull(message = "es obligatorio")
    @DecimalMin(value = "0.00", message = "no puede ser negativo")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal costoTotal;

    @NotNull(message = "es obligatorio")
    @Column(nullable = false)
    private Boolean servicioCerrado;

    @Valid
    @NotEmpty(message = "debe contener al menos un servicio realizado")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "registro_servicios_realizados",
            joinColumns = @JoinColumn(name = "registro_id"))
    private List<ServicioRealizadoResumen> serviciosRealizados = new ArrayList<>();

    @Valid
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "registro_productos_utilizados",
            joinColumns = @JoinColumn(name = "registro_id"))
    private List<ProductoUtilizadoResumen> productosUtilizados = new ArrayList<>();

    @Valid
    @NotNull(message = "es obligatorio")
    @Embedded
    private ProximoServicio proximoServicio;

    public RegistroMantenimiento() {}

    public boolean correspondeAServicioCerrado() {
        return Boolean.TRUE.equals(servicioCerrado);
    }

    public boolean tieneProximoServicioDefinido() {
        return proximoServicio != null && proximoServicio.estaDefinido();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaAtencion() {
        return fechaAtencion;
    }

    public void setFechaAtencion(LocalDate fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }

    public Long getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(Long kilometraje) {
        this.kilometraje = kilometraje;
    }

    public Long getOrdenId() {
        return ordenId;
    }

    public void setOrdenId(Long ordenId) {
        this.ordenId = ordenId;
    }

    public Long getTecnicoId() {
        return tecnicoId;
    }

    public void setTecnicoId(Long tecnicoId) {
        this.tecnicoId = tecnicoId;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    public Boolean getServicioCerrado() {
        return servicioCerrado;
    }

    public void setServicioCerrado(Boolean servicioCerrado) {
        this.servicioCerrado = servicioCerrado;
    }

    public List<ServicioRealizadoResumen> getServiciosRealizados() {
        return serviciosRealizados;
    }

    public void setServiciosRealizados(List<ServicioRealizadoResumen> serviciosRealizados) {
        this.serviciosRealizados = serviciosRealizados;
    }

    public List<ProductoUtilizadoResumen> getProductosUtilizados() {
        return productosUtilizados;
    }

    public void setProductosUtilizados(List<ProductoUtilizadoResumen> productosUtilizados) {
        this.productosUtilizados = productosUtilizados;
    }

    public ProximoServicio getProximoServicio() {
        return proximoServicio;
    }

    public void setProximoServicio(ProximoServicio proximoServicio) {
        this.proximoServicio = proximoServicio;
    }
}
