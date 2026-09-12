package org.lubrimax.msvc.vehiculos.models.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Embeddable
public class FichaTecnica {

    @NotBlank(message = "es obligatoria")
    private String marca;

    @NotBlank(message = "es obligatorio")
    private String modelo;

    @NotNull(message = "es obligatorio")
    @Min(value = 1950, message = "debe ser mayor o igual a 1950")
    @Max(value = 2100, message = "no tiene un valor válido")
    private Integer anio;

    @NotBlank(message = "es obligatorio")
    private String motor;

    @NotNull(message = "es obligatoria")
    @Positive(message = "debe ser mayor que cero")
    private Integer cilindradaCc;

    @NotNull(message = "es obligatorio")
    @Enumerated(EnumType.STRING)
    private TipoCombustible tipoCombustible;

    public FichaTecnica() {
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public Integer getCilindradaCc() {
        return cilindradaCc;
    }

    public void setCilindradaCc(Integer cilindradaCc) {
        this.cilindradaCc = cilindradaCc;
    }

    public TipoCombustible getTipoCombustible() {
        return tipoCombustible;
    }

    public void setTipoCombustible(TipoCombustible tipoCombustible) {
        this.tipoCombustible = tipoCombustible;
    }
}
