package org.lubrimax.msvc.clientes.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "es obligatorio")
    @Column(nullable = false, length = 150)
    private String nombre;

    @NotBlank(message = "es obligatorio")
    @Column(
            name = "numero_documento",
            nullable = false,
            unique = true,
            updatable = false,
            length = 20)
    private String numeroDocumento;

    @NotBlank(message = "es obligatorio")
    @Pattern(regexp = "[0-9+() -]{7,20}", message = "no tiene un formato válido")
    @Column(nullable = false, length = 20)
    private String telefono;

    public Cliente() {}

    public Cliente(String nombre, String numeroDocumento, String telefono) {
        this.nombre = nombre;
        this.numeroDocumento = numeroDocumento;
        this.telefono = telefono;
    }

    public void actualizarDatosDeContacto(String nombre, String telefono) {
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public boolean admiteAutorizacionRemota() {
        return telefono != null && telefono.replaceAll("\\D", "").length() >= 7;
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

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
