package org.lubrimax.msvc_ejecucion_mantenimiento.models.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.CantidadDeProducto;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.Dinero;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.EstadoDeEjecucion;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.EstadoDeTarea;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.Kilometraje;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.LiquidacionDeServicio;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.ObservacionPreventiva;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.ProductoAutorizado;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.ProgramacionDeProximoServicio;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.ServicioAutorizado;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.TipoDeResiduoGenerado;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.TipoDeTarea;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.VolumenDeclarado;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "ejecuciones_mantenimiento")
public class EjecucionDeMantenimiento {

    @Id
    private Long ordenId;

    @Version
    private int version;

    private Long tecnicoUsuarioId;

    @Embedded
    private Kilometraje kilometraje;

    @Enumerated(EnumType.STRING)
    private EstadoDeEjecucion estado;

    private LocalDateTime inicio;
    private LocalDateTime cierre;

    @Embedded
    private LiquidacionDeServicio liquidacion;

    @Embedded
    private ProgramacionDeProximoServicio proximoServicio;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "orden_id", nullable = false)
    private List<TareaEjecutada> tareas = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "orden_id", nullable = false)
    private List<ConsumoReal> consumos = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "orden_id", nullable = false)
    private List<ResiduoDeclarado> residuos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "observaciones_preventivas", joinColumns = @JoinColumn(name = "orden_id"))
    private List<ObservacionPreventiva> observaciones = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "servicios_autorizados", joinColumns = @JoinColumn(name = "orden_id"))
    private List<ServicioAutorizado> serviciosAutorizados = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "productos_autorizados", joinColumns = @JoinColumn(name = "orden_id"))
    private List<ProductoAutorizado> productosAutorizados = new ArrayList<>();

    protected EjecucionDeMantenimiento() {
    }

    public EjecucionDeMantenimiento(Long ordenId, Long tecnicoUsuarioId, Kilometraje kilometraje,
                                    List<ServicioAutorizado> serviciosAutorizados,
                                    List<ProductoAutorizado> productosAutorizados) {
        if (ordenId == null) throw new IllegalArgumentException("La orden es obligatoria");
        if (tecnicoUsuarioId == null) throw new IllegalArgumentException("El tecnico responsable es obligatorio");
        if (kilometraje == null) throw new IllegalArgumentException("El kilometraje es obligatorio");
        this.ordenId = ordenId;
        this.tecnicoUsuarioId = tecnicoUsuarioId;
        this.kilometraje = kilometraje;
        this.estado = EstadoDeEjecucion.PENDIENTE;
        if (serviciosAutorizados != null) this.serviciosAutorizados.addAll(serviciosAutorizados);
        if (productosAutorizados != null) this.productosAutorizados.addAll(productosAutorizados);
    }

    public void iniciar(boolean ordenAutorizadaYEmitida, boolean asignacionValida) {
        asegurarNoCerrada();
        if (!ordenAutorizadaYEmitida) throw new IllegalStateException("La orden debe estar autorizada y emitida");
        if (!asignacionValida) throw new IllegalStateException("El tecnico no tiene una asignacion valida para la orden");
        if (estado != EstadoDeEjecucion.PENDIENTE && estado != EstadoDeEjecucion.SUSPENDIDA) {
            throw new IllegalStateException("Solo una ejecucion PENDIENTE o SUSPENDIDA puede iniciarse");
        }
        if (inicio == null) inicio = LocalDateTime.now();
        estado = EstadoDeEjecucion.EN_CURSO;
    }

    public void suspender() {
        asegurarNoCerrada();
        if (estado != EstadoDeEjecucion.EN_CURSO) throw new IllegalStateException("Solo una ejecucion EN_CURSO puede suspenderse");
        estado = EstadoDeEjecucion.SUSPENDIDA;
    }

    public TareaEjecutada registrarTarea(Long servicioId, TipoDeTarea tipo) {
        exigirEnCurso();
        ServicioAutorizado autorizado = serviciosAutorizados.stream()
                .filter(servicio -> servicio.getServicioId().equals(servicioId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("La tarea no corresponde a un servicio autorizado"));
        TareaEjecutada tarea = new TareaEjecutada(servicioId, tipo, autorizado.getPrecio());
        tareas.add(tarea);
        return tarea;
    }

    public void iniciarTarea(Long tareaId) { exigirEnCurso(); buscarTarea(tareaId).iniciar(); }
    public void completarTarea(Long tareaId) { exigirEnCurso(); buscarTarea(tareaId).completar(); }
    public void marcarTareaNoRealizada(Long tareaId) { exigirEnCurso(); buscarTarea(tareaId).marcarNoRealizada(); }

    public ConsumoReal registrarConsumo(Long productoId, CantidadDeProducto cantidad) {
        exigirEnCurso();
        ProductoAutorizado autorizado = productosAutorizados.stream()
                .filter(producto -> producto.getProductoId().equals(productoId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("El producto consumido no fue autorizado"));
        ConsumoReal consumo = new ConsumoReal(productoId, cantidad, autorizado.getPrecio());
        consumos.add(consumo);
        return consumo;
    }

    public ResiduoDeclarado declararResiduo(Long tareaId, TipoDeResiduoGenerado tipo, VolumenDeclarado volumen) {
        exigirEnCurso();
        TareaEjecutada tarea = buscarTarea(tareaId);
        if (tarea.getEstado() != EstadoDeTarea.COMPLETADA || !tarea.getTipo().generaResiduo()) {
            throw new IllegalStateException("Solo una tarea completada que genere residuos puede tener una declaracion");
        }
        if (tarea.getTipo().getResiduoEsperado() != tipo) {
            throw new IllegalArgumentException("El tipo de residuo no corresponde a la tarea");
        }
        ResiduoDeclarado residuo = new ResiduoDeclarado(tareaId, tipo, volumen);
        residuos.add(residuo);
        return residuo;
    }

    public void registrarObservacion(ObservacionPreventiva observacion) {
        exigirEnCurso();
        if (observacion == null) throw new IllegalArgumentException("La observacion es obligatoria");
        observaciones.add(observacion);
    }

    public LiquidacionDeServicio calcularLiquidacion() {
        asegurarNoCerrada();
        String moneda = determinarMoneda();
        Dinero manoDeObra = tareas.stream().filter(tarea -> tarea.getEstado() == EstadoDeTarea.COMPLETADA)
                .map(tarea -> tarea.getPrecioAplicado().comoDinero())
                .reduce(new Dinero(BigDecimal.ZERO, moneda), Dinero::sumar);
        Dinero productos = consumos.stream().map(ConsumoReal::subtotal)
                .reduce(new Dinero(BigDecimal.ZERO, moneda), Dinero::sumar);
        liquidacion = new LiquidacionDeServicio(productos, manoDeObra);
        return liquidacion;
    }

    public void definirProximoServicio(ProgramacionDeProximoServicio programacion) {
        asegurarNoCerrada();
        if (programacion == null) throw new IllegalArgumentException("La programacion es obligatoria");
        this.proximoServicio = programacion;
    }

    public void validarCierre() {
        exigirEnCurso();
        if (tareas.stream().anyMatch(tarea -> tarea.getEstado() == EstadoDeTarea.EN_CURSO)) {
            throw new IllegalStateException("No se puede cerrar con tareas EN_CURSO");
        }
        boolean faltaResiduo = tareas.stream()
                .filter(tarea -> tarea.getEstado() == EstadoDeTarea.COMPLETADA && tarea.getTipo().generaResiduo())
                .anyMatch(tarea -> residuos.stream().noneMatch(residuo -> residuo.getTareaId().equals(tarea.getId())));
        if (faltaResiduo) throw new IllegalStateException("Falta declarar el residuo de una tarea que lo genera");
        if (proximoServicio == null) throw new IllegalStateException("Debe definirse el proximo servicio antes de cerrar");
    }

    public void cerrar() {
        validarCierre();
        calcularLiquidacion();
        estado = EstadoDeEjecucion.EJECUTADA;
        cierre = LocalDateTime.now();
        estado = EstadoDeEjecucion.CERRADA;
    }

    private String determinarMoneda() {
        if (!serviciosAutorizados.isEmpty()) return serviciosAutorizados.get(0).getPrecio().getMoneda();
        if (!productosAutorizados.isEmpty()) return productosAutorizados.get(0).getPrecio().getMoneda();
        return "PEN";
    }

    private TareaEjecutada buscarTarea(Long tareaId) {
        return tareas.stream().filter(tarea -> tarea.getId().equals(tareaId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No se encontro la tarea con ID: " + tareaId));
    }

    private void exigirEnCurso() {
        asegurarNoCerrada();
        if (estado != EstadoDeEjecucion.EN_CURSO) throw new IllegalStateException("La ejecucion debe estar EN_CURSO");
    }

    private void asegurarNoCerrada() {
        if (estado == EstadoDeEjecucion.CERRADA) throw new IllegalStateException("Una ejecucion CERRADA es inmutable");
    }

    public Long getOrdenId() { return ordenId; }
    public Long getTecnicoUsuarioId() { return tecnicoUsuarioId; }
    public Kilometraje getKilometraje() { return kilometraje; }
    public EstadoDeEjecucion getEstado() { return estado; }
    public LocalDateTime getInicio() { return inicio; }
    public LocalDateTime getCierre() { return cierre; }
    public LiquidacionDeServicio getLiquidacion() { return liquidacion; }
    public ProgramacionDeProximoServicio getProximoServicio() { return proximoServicio; }
    public List<TareaEjecutada> getTareas() { return Collections.unmodifiableList(tareas); }
    public List<ConsumoReal> getConsumos() { return Collections.unmodifiableList(consumos); }
    public List<ResiduoDeclarado> getResiduos() { return Collections.unmodifiableList(residuos); }
    public List<ObservacionPreventiva> getObservaciones() { return Collections.unmodifiableList(observaciones); }
    public List<ServicioAutorizado> getServiciosAutorizados() { return Collections.unmodifiableList(serviciosAutorizados); }
    public List<ProductoAutorizado> getProductosAutorizados() { return Collections.unmodifiableList(productosAutorizados); }
}
