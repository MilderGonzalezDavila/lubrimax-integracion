package org.lubrimax.msvc_entrega_operador.models.entity;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.Version;

import org.lubrimax.msvc_entrega_operador.models.AutorizacionDelOperador;
import org.lubrimax.msvc_entrega_operador.models.CantidadDeResiduo;
import org.lubrimax.msvc_entrega_operador.models.EstadoDeEntrega;
import org.lubrimax.msvc_entrega_operador.models.ManifiestoDeResiduos;
import org.lubrimax.msvc_entrega_operador.models.TipoDeResiduo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "entregas_operador")
public class EntregaAOperador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version private int version;

    private Long operadorId;

    private Long usuarioResponsableId;

    private LocalDate fechaEntrega;

    @Enumerated(EnumType.STRING)
    private EstadoDeEntrega estado;

    @Embedded private AutorizacionDelOperador autorizacionDelOperador;

    @Embedded private ManifiestoDeResiduos manifiesto;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "entrega_id", nullable = false)
    private List<LineaDeEntrega> lineas = new ArrayList<>();

    protected EntregaAOperador() {}

    public EntregaAOperador(Long operadorId, Long usuarioResponsableId, LocalDate fechaEntrega) {
        if (operadorId == null) {
            throw new IllegalArgumentException("El operador es obligatorio");
        }
        if (usuarioResponsableId == null) {
            throw new IllegalArgumentException("El usuario responsable es obligatorio");
        }
        if (fechaEntrega == null) {
            throw new IllegalArgumentException("La fecha de entrega es obligatoria");
        }
        this.operadorId = operadorId;
        this.usuarioResponsableId = usuarioResponsableId;
        this.fechaEntrega = fechaEntrega;
        this.estado = EstadoDeEntrega.PROGRAMADA;
    }

    public LineaDeEntrega agregarLinea(
            TipoDeResiduo tipo, CantidadDeResiduo cantidad, List<Long> residuosIds) {
        exigirProgramada();
        boolean residuoRepetido =
                lineas.stream()
                        .flatMap(linea -> linea.getResiduosIds().stream())
                        .anyMatch(residuosIds::contains);
        if (residuoRepetido) {
            throw new IllegalArgumentException("Un residuo no puede aparecer en dos lineas");
        }
        LineaDeEntrega linea = new LineaDeEntrega(tipo, cantidad, residuosIds);
        lineas.add(linea);
        return linea;
    }

    public void eliminarLinea(Long lineaId) {
        exigirProgramada();
        boolean eliminada = lineas.removeIf(linea -> linea.getId().equals(lineaId));
        if (!eliminada) {
            throw new IllegalArgumentException("No se encontro la linea con ID: " + lineaId);
        }
    }

    public void registrarManifiesto(ManifiestoDeResiduos manifiesto) {
        exigirProgramada();
        if (manifiesto == null) {
            throw new IllegalArgumentException("El manifiesto es obligatorio");
        }
        this.manifiesto = manifiesto;
    }

    public void ejecutar(AutorizacionDelOperador autorizacion) {
        exigirProgramada();
        if (lineas.isEmpty()) {
            throw new IllegalStateException("La entrega debe tener al menos una linea");
        }
        if (manifiesto == null) {
            throw new IllegalStateException(
                    "Debe registrar el manifiesto antes de ejecutar la entrega");
        }
        if (autorizacion == null || !autorizacion.estaVigenteAl(fechaEntrega)) {
            throw new IllegalStateException("El operador no esta vigente en la fecha de entrega");
        }
        this.autorizacionDelOperador = autorizacion;
        this.estado = EstadoDeEntrega.EJECUTADA;
    }

    public void conformar() {
        if (estado == EstadoDeEntrega.CONFORMADA) {
            return;
        }
        if (estado != EstadoDeEntrega.EJECUTADA) {
            throw new IllegalStateException("Solo una entrega EJECUTADA puede conformarse");
        }
        estado = EstadoDeEntrega.CONFORMADA;
    }

    private void exigirProgramada() {
        if (estado != EstadoDeEntrega.PROGRAMADA) {
            throw new IllegalStateException(
                    "La operacion solo se permite mientras la entrega esta PROGRAMADA");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getOperadorId() {
        return operadorId;
    }

    public Long getUsuarioResponsableId() {
        return usuarioResponsableId;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public EstadoDeEntrega getEstado() {
        return estado;
    }

    public AutorizacionDelOperador getAutorizacionDelOperador() {
        return autorizacionDelOperador;
    }

    public ManifiestoDeResiduos getManifiesto() {
        return manifiesto;
    }

    public List<LineaDeEntrega> getLineas() {
        return Collections.unmodifiableList(lineas);
    }
}
