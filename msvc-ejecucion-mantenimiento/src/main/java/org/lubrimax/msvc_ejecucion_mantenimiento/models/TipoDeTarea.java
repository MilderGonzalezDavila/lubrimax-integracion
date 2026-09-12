package org.lubrimax.msvc_ejecucion_mantenimiento.models;

public enum TipoDeTarea {
    DRENAJE_ACEITE(TipoDeResiduoGenerado.ACEITE_USADO),
    CAMBIO_FILTRO(TipoDeResiduoGenerado.FILTRO_USADO),
    APLICACION_ADITIVO(TipoDeResiduoGenerado.ENVASE_CONTAMINADO),
    REVISION_NIVELES(null);

    private final TipoDeResiduoGenerado residuoEsperado;

    TipoDeTarea(TipoDeResiduoGenerado residuoEsperado) {
        this.residuoEsperado = residuoEsperado;
    }

    public boolean generaResiduo() {
        return residuoEsperado != null;
    }

    public TipoDeResiduoGenerado getResiduoEsperado() {
        return residuoEsperado;
    }
}
