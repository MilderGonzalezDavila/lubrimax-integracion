package org.lubrimax.msvc_usuarios.models.values;

import jakarta.persistence.Embeddable;

@Embeddable
public class Permiso {

    private String recurso;
    private String accion;

    public Permiso() {}

    public Permiso(String recurso, String accion) {
        this.recurso = recurso;
        this.accion = accion;
    }

    public String getRecurso() {
        return recurso;
    }

    public String getAccion() {
        return accion;
    }
}
