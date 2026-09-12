package org.lubrimax.msvc_matriz_compatibilidad.models;

import jakarta.persistence.Embeddable;

@Embeddable
public class CriterioDeCompatibilidad {

    private String tipoDeMotor;
    private Integer cilindrada;
    private String combustible;
    private Integer anioDesde;
    private Integer anioHasta;

    public boolean aplicaA(String motor, Integer cc, String combustible, Integer anio) {
        return coincide(tipoDeMotor, motor)
                && (cilindrada == null || cilindrada.equals(cc))
                && coincide(this.combustible, combustible)
                && (anioDesde == null || anio >= anioDesde)
                && (anioHasta == null || anio <= anioHasta);
    }

    private boolean coincide(String esperado, String actual) {
        return esperado == null || esperado.isBlank() || esperado.equalsIgnoreCase(actual);
    }

    public String getTipoDeMotor() {
        return tipoDeMotor;
    }

    public void setTipoDeMotor(String v) {
        tipoDeMotor = v;
    }

    public Integer getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(Integer v) {
        cilindrada = v;
    }

    public String getCombustible() {
        return combustible;
    }

    public void setCombustible(String v) {
        combustible = v;
    }

    public Integer getAnioDesde() {
        return anioDesde;
    }

    public void setAnioDesde(Integer v) {
        anioDesde = v;
    }

    public Integer getAnioHasta() {
        return anioHasta;
    }

    public void setAnioHasta(Integer v) {
        anioHasta = v;
    }
}
