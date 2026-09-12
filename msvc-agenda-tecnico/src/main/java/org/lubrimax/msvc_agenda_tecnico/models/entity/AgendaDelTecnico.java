package org.lubrimax.msvc_agenda_tecnico.models.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import org.lubrimax.msvc_agenda_tecnico.models.EstadoDeAsignacion;
import org.lubrimax.msvc_agenda_tecnico.models.PeriodoDeTrabajo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "agendas_tecnicos")
public class AgendaDelTecnico {

    @Id
    private Long usuarioId;

    @Version
    private int version;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "tecnico_usuario_id", nullable = false)
    private List<AsignacionDeTrabajo> asignaciones = new ArrayList<>();

    protected AgendaDelTecnico() {
    }

    public AgendaDelTecnico(Long usuarioId) {
        if (usuarioId == null) throw new IllegalArgumentException("El usuario tecnico es obligatorio");
        this.usuarioId = usuarioId;
    }

    public AsignacionDeTrabajo asignar(Long ordenId, PeriodoDeTrabajo periodo, boolean tecnicoActivo) {
        if (!tecnicoActivo) {
            throw new IllegalArgumentException("El usuario no tiene un rol de tecnico activo");
        }
        if (!estaDisponible(periodo)) {
            throw new IllegalStateException("El tecnico ya tiene una asignacion que se solapa con ese periodo");
        }
        AsignacionDeTrabajo asignacion = new AsignacionDeTrabajo(ordenId, periodo);
        asignaciones.add(asignacion);
        return asignacion;
    }

    public boolean estaDisponible(PeriodoDeTrabajo periodo) {
        return asignaciones.stream().noneMatch(asignacion ->
                asignacion.ocupaElPeriodo() && asignacion.getPeriodo().seSolapaCon(periodo));
    }

    public void iniciarAsignacion(Long asignacionId) {
        buscarAsignacion(asignacionId).iniciar();
    }

    public void liberarAsignacion(Long asignacionId) {
        buscarAsignacion(asignacionId).liberar();
    }

    public boolean tieneAsignacionValida(Long ordenId) {
        return asignaciones.stream().anyMatch(asignacion -> ordenId.equals(asignacion.getOrdenId())
                && asignacion.getEstado() != EstadoDeAsignacion.LIBERADA);
    }

    private AsignacionDeTrabajo buscarAsignacion(Long asignacionId) {
        return asignaciones.stream().filter(asignacion -> asignacion.getId().equals(asignacionId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No se encontro la asignacion con ID: " + asignacionId));
    }

    public Long getUsuarioId() { return usuarioId; }
    public List<AsignacionDeTrabajo> getAsignaciones() { return Collections.unmodifiableList(asignaciones); }
}
