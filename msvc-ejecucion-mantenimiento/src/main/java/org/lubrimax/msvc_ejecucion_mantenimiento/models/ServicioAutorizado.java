package org.lubrimax.msvc_ejecucion_mantenimiento.models;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public class ServicioAutorizado {
    @Column(name = "servicio_id", nullable = false)
    private Long servicioId;

    @Embedded
    @AttributeOverride(name = "monto", column = @Column(name = "precio_servicio", nullable = false, precision = 12, scale = 2))
    @AttributeOverride(name = "moneda", column = @Column(name = "moneda_servicio", nullable = false, length = 3))
    @AttributeOverride(name = "momento", column = @Column(name = "momento_precio_servicio", nullable = false))
    private PrecioAplicado precio;

    protected ServicioAutorizado() {}
    public ServicioAutorizado(Long servicioId, PrecioAplicado precio) {
        if (servicioId == null || precio == null) throw new IllegalArgumentException("El servicio y su precio son obligatorios");
        this.servicioId = servicioId;
        this.precio = precio;
    }
    public Long getServicioId() { return servicioId; }
    public PrecioAplicado getPrecio() { return precio; }
}
