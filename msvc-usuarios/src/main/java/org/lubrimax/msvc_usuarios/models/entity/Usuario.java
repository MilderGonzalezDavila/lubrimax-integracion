package org.lubrimax.msvc_usuarios.models.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String documentoIdentidad;
    private String credencialAccesoEstado;
    private String credencial;
    private Long rolId;

    public void cambiarEstado(String nuevoEstado) {
        this.credencialAccesoEstado = nuevoEstado;
    }

    public void resetearCredencial(String nuevaCredencial) {
        this.credencial = nuevaCredencial;
    }

    public String getCredencial() {
        return credencial;
    }

    public void setCredencial(String credencial) {
        this.credencial = credencial;
    }

    public String getCredencialAccesoEstado() {
        return credencialAccesoEstado;
    }

    public void setCredencialAccesoEstado(String credencialAccesoEstado) {
        this.credencialAccesoEstado = credencialAccesoEstado;
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public void setDocumentoIdentidad(String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getRolId() {
        return rolId;
    }

    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }
}
