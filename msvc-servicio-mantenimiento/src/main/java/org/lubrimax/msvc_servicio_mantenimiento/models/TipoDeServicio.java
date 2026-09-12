package org.lubrimax.msvc_servicio_mantenimiento.models;

public enum TipoDeServicio {
    CAMBIO_ACEITE,
    CAMBIO_FILTRO,
    APLICACION_ADITIVO,
    REVISION_NIVELES;

    public boolean esPreventivoLigero() {
        return true;
    }
}
