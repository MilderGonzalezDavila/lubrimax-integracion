package org.lubrimax.msvc_agregar_producto.models;

import jakarta.persistence.Embeddable;

@Embeddable
public class EspecificacionTecnica {

    private String normaApi;
    private String normaAcea;

    public String getNormaApi() {
        return normaApi;
    }

    public void setNormaApi(String normaApi) {
        this.normaApi = normaApi;
    }

    public String getNormaAcea() {
        return normaAcea;
    }

    public void setNormaAcea(String normaAcea) {
        this.normaAcea = normaAcea;
    }
}
