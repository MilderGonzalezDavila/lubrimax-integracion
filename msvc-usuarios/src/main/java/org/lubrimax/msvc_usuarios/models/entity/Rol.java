package org.lubrimax.msvc_usuarios.models.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import org.lubrimax.msvc_usuarios.models.values.Permiso;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Equivalente a RolId del diagrama

    private String nombre;

    @ElementCollection
    @CollectionTable(name = "rol_permisos", joinColumns = @JoinColumn(name = "rol_id"))
    private List<Permiso> permisos = new ArrayList<>();

    // Comandos de dominio[cite: 3]
    public void otorgarPermiso(Permiso permiso) {
        this.permisos.add(permiso);
    }

    public void revocarPermiso(Permiso permiso) {
        this.permisos.remove(permiso);
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

    public List<Permiso> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<Permiso> permisos) {
        this.permisos = permisos;
    }
}
