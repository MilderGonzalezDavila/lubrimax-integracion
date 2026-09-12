package org.lubrimax.msvc_servicio_mantenimiento.models.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.lubrimax.msvc_servicio_mantenimiento.models.CategoriaDeProducto;
import org.lubrimax.msvc_servicio_mantenimiento.models.TipoDeServicio;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "servicios_mantenimiento")
public class ServicioDeMantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version private int version;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private TipoDeServicio tipo;

    @NotBlank
    @Column(nullable = false)
    private String descripcion;

    private boolean activo = true;

    @NotEmpty
    @ElementCollection(targetClass = CategoriaDeProducto.class)
    @CollectionTable(name = "servicio_categorias", joinColumns = @JoinColumn(name = "servicio_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    private Set<CategoriaDeProducto> categoriasConsumidas = new HashSet<>();

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public TipoDeServicio getTipo() {
        return tipo;
    }

    public void setTipo(TipoDeServicio tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Set<CategoriaDeProducto> getCategoriasConsumidas() {
        return categoriasConsumidas;
    }

    public void setCategoriasConsumidas(Set<CategoriaDeProducto> categoriasConsumidas) {
        this.categoriasConsumidas = categoriasConsumidas;
    }
}
