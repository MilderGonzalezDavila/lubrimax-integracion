package org.lubrimax.msvc_operador_autorizado.models.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import org.lubrimax.msvc_operador_autorizado.models.AutorizacionDelOperador;
import org.lubrimax.msvc_operador_autorizado.models.Ruc;
import org.lubrimax.msvc_operador_autorizado.models.Telefono;

import java.time.LocalDate;

@Entity
@Table(name = "operadores_autorizados")
public class OperadorAutorizado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    @Embedded
    private Ruc ruc;

    private String razonSocial;

    @Embedded
    private Telefono telefono;

    @Embedded
    private AutorizacionDelOperador autorizacion;

    protected OperadorAutorizado() {
    }

    public OperadorAutorizado(Ruc ruc, String razonSocial, Telefono telefono,
                              AutorizacionDelOperador autorizacion) {
        validarDatos(ruc, razonSocial, telefono);
        if (autorizacion == null) {
            throw new IllegalArgumentException("La autorizacion del operador es obligatoria");
        }
        this.ruc = ruc;
        this.razonSocial = razonSocial.trim();
        this.telefono = telefono;
        this.autorizacion = autorizacion;
    }

    public void actualizarDatos(String razonSocial, Telefono telefono) {
        validarDatos(this.ruc, razonSocial, telefono);
        this.razonSocial = razonSocial.trim();
        this.telefono = telefono;
    }

    public void actualizarAutorizacion(AutorizacionDelOperador nuevaAutorizacion) {
        if (nuevaAutorizacion == null) {
            throw new IllegalArgumentException("La autorizacion del operador es obligatoria");
        }
        this.autorizacion = nuevaAutorizacion;
    }

    public boolean estaVigenteAl(LocalDate fecha) {
        return autorizacion.estaVigenteAl(fecha);
    }

    private void validarDatos(Ruc ruc, String razonSocial, Telefono telefono) {
        if (ruc == null) throw new IllegalArgumentException("El RUC es obligatorio");
        if (razonSocial == null || razonSocial.isBlank()) {
            throw new IllegalArgumentException("La razon social es obligatoria");
        }
        if (telefono == null) throw new IllegalArgumentException("El telefono es obligatorio");
    }

    public Long getId() { return id; }
    public Ruc getRuc() { return ruc; }
    public String getRazonSocial() { return razonSocial; }
    public Telefono getTelefono() { return telefono; }
    public AutorizacionDelOperador getAutorizacion() { return autorizacion; }
}
