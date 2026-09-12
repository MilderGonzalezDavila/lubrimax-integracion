package org.lubrimax.msvc_agregar_producto.models.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.lubrimax.msvc_agregar_producto.models.CategoriaDeProducto;
import org.lubrimax.msvc_agregar_producto.models.EspecificacionTecnica;
import org.lubrimax.msvc_agregar_producto.models.EstadoDelProducto;
import org.lubrimax.msvc_agregar_producto.models.IntervaloDeServicio;
import org.lubrimax.msvc_agregar_producto.models.TipoDeAceite;
import org.lubrimax.msvc_agregar_producto.models.TipoDeFiltro;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productos", uniqueConstraints = @UniqueConstraint(columnNames = "codigo"))
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version private int version;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String codigo;

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    @NotBlank
    @Column(nullable = false)
    private String marca;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaDeProducto categoria;

    @Enumerated(EnumType.STRING)
    private TipoDeAceite tipoAceite;

    private String viscosidad;

    @Valid @Embedded private EspecificacionTecnica especificacion;

    @Enumerated(EnumType.STRING)
    private TipoDeFiltro tipoFiltro;

    @Valid @Embedded private IntervaloDeServicio intervalo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDelProducto estado = EstadoDelProducto.ACTIVO;

    @Valid
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "producto_id", nullable = false)
    private List<PresentacionDeProducto> presentaciones = new ArrayList<>();

    public void desactivar() {
        estado = EstadoDelProducto.INACTIVO;
    }

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public CategoriaDeProducto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDeProducto categoria) {
        this.categoria = categoria;
    }

    public TipoDeAceite getTipoAceite() {
        return tipoAceite;
    }

    public void setTipoAceite(TipoDeAceite tipoAceite) {
        this.tipoAceite = tipoAceite;
    }

    public String getViscosidad() {
        return viscosidad;
    }

    public void setViscosidad(String viscosidad) {
        this.viscosidad = viscosidad;
    }

    public EspecificacionTecnica getEspecificacion() {
        return especificacion;
    }

    public void setEspecificacion(EspecificacionTecnica especificacion) {
        this.especificacion = especificacion;
    }

    public TipoDeFiltro getTipoFiltro() {
        return tipoFiltro;
    }

    public void setTipoFiltro(TipoDeFiltro tipoFiltro) {
        this.tipoFiltro = tipoFiltro;
    }

    public IntervaloDeServicio getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(IntervaloDeServicio intervalo) {
        this.intervalo = intervalo;
    }

    public EstadoDelProducto getEstado() {
        return estado;
    }

    public void setEstado(EstadoDelProducto estado) {
        this.estado = estado;
    }

    public List<PresentacionDeProducto> getPresentaciones() {
        return presentaciones;
    }

    public void setPresentaciones(List<PresentacionDeProducto> presentaciones) {
        this.presentaciones = presentaciones == null ? new ArrayList<>() : presentaciones;
    }
}
