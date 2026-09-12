package org.lubrimax.msvc_matriz_compatibilidad.models.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "matrices_compatibilidad")
public class MatrizDeCompatibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version private int version;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "matriz_id")
    private List<ReglaDeCompatibilidad> reglas = new ArrayList<>();

    public boolean esCompatible(
            String motor, Integer cc, String combustible, Integer anio, Long productoId) {
        return reglas.stream()
                .filter(r -> r.aplicaA(motor, cc, combustible, anio))
                .anyMatch(r -> r.getProductosAdmitidos().contains(productoId));
    }

    public Set<Long> productosPara(String motor, Integer cc, String combustible, Integer anio) {
        Set<Long> ids = new LinkedHashSet<>();
        reglas.stream()
                .filter(r -> r.aplicaA(motor, cc, combustible, anio))
                .forEach(r -> ids.addAll(r.getProductosAdmitidos()));
        return ids;
    }

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public List<ReglaDeCompatibilidad> getReglas() {
        return reglas;
    }

    public void setReglas(List<ReglaDeCompatibilidad> v) {
        reglas = v == null ? new ArrayList<>() : v;
    }
}
