package org.lubrimax.msvc_entrega_operador.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public class AutorizacionDelOperador {

    @Column(name = "operador_ruc", nullable = false, length = 11)
    private String ruc;

    @Column(name = "operador_razon_social", nullable = false)
    private String razonSocial;

    @Column(name = "registro_eors", nullable = false)
    private String registroEors;

    @Column(name = "vigencia_desde", nullable = false)
    private LocalDate desde;

    @Column(name = "vigencia_hasta", nullable = false)
    private LocalDate hasta;

    protected AutorizacionDelOperador() {}

    public AutorizacionDelOperador(
            String ruc, String razonSocial, String registroEors, LocalDate desde, LocalDate hasta) {
        if (ruc == null
                || ruc.isBlank()
                || razonSocial == null
                || razonSocial.isBlank()
                || registroEors == null
                || registroEors.isBlank()
                || desde == null
                || hasta == null) {
            throw new IllegalArgumentException(
                    "Los datos de autorizacion del operador son obligatorios");
        }
        this.ruc = ruc;
        this.razonSocial = razonSocial;
        this.registroEors = registroEors;
        this.desde = desde;
        this.hasta = hasta;
    }

    public boolean estaVigenteAl(LocalDate fecha) {
        return fecha != null && !fecha.isBefore(desde) && !fecha.isAfter(hasta);
    }

    public String getRuc() {
        return ruc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public String getRegistroEors() {
        return registroEors;
    }

    public LocalDate getDesde() {
        return desde;
    }

    public LocalDate getHasta() {
        return hasta;
    }
}
