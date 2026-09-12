package org.lubrimax.msvc_auditorias.models.values;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public class Justificacion {

    private String motivo;
    private Long usuarioIdResponsable;
    private LocalDateTime momento;

    public Justificacion() {}

    public Justificacion(String motivo, Long usuarioIdResponsable, LocalDateTime momento) {
        this.motivo = motivo;
        this.usuarioIdResponsable = usuarioIdResponsable;
        this.momento = momento;
    }

    public String getMotivo() { return motivo; }
    public Long getUsuarioIdResponsable() { return usuarioIdResponsable; }
    public LocalDateTime getMomento() { return momento; }
}
