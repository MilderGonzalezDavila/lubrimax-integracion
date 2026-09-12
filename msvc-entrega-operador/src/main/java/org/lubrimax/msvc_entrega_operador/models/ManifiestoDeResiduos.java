package org.lubrimax.msvc_entrega_operador.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.time.LocalDate;

@Embeddable
public class ManifiestoDeResiduos {

    @Column(name = "manifiesto_numero")
    private String numero;

    @Column(name = "manifiesto_fecha")
    private LocalDate fecha;

    @Column(name = "manifiesto_cantidad_total", precision = 12, scale = 3)
    private BigDecimal cantidadTotal;

    @Column(name = "manifiesto_unidad", length = 20)
    private String unidad;

    @Column(name = "responsable_recepcion")
    private String responsableRecepcion;

    protected ManifiestoDeResiduos() {
    }

    public ManifiestoDeResiduos(String numero, LocalDate fecha, BigDecimal cantidadTotal,
                                String unidad, String responsableRecepcion) {
        if (numero == null || numero.isBlank()) throw new IllegalArgumentException("El numero del manifiesto es obligatorio");
        if (fecha == null) throw new IllegalArgumentException("La fecha del manifiesto es obligatoria");
        if (cantidadTotal == null || cantidadTotal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad total del manifiesto debe ser mayor que cero");
        }
        if (unidad == null || unidad.isBlank()) throw new IllegalArgumentException("La unidad del manifiesto es obligatoria");
        if (responsableRecepcion == null || responsableRecepcion.isBlank()) {
            throw new IllegalArgumentException("El responsable de recepcion es obligatorio");
        }
        this.numero = numero.trim();
        this.fecha = fecha;
        this.cantidadTotal = cantidadTotal;
        this.unidad = unidad.trim().toUpperCase();
        this.responsableRecepcion = responsableRecepcion.trim();
    }

    public String getNumero() { return numero; }
    public LocalDate getFecha() { return fecha; }
    public BigDecimal getCantidadTotal() { return cantidadTotal; }
    public String getUnidad() { return unidad; }
    public String getResponsableRecepcion() { return responsableRecepcion; }
}
