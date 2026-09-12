package org.lubrimax.msvc_auditorias.models.entity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "auditorias")
public class AuditoriaDeControlInterno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;
    private String periodo;
    private String estado;
    private Long usuarioIdResponsable;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "auditoria_id")
    private List<HallazgoDeAuditoria> hallazgos = new ArrayList<>();

    public AuditoriaDeControlInterno() {}

    public void inicializar() {
        this.estado = "ABIERTA";
    }

    public void registrarHallazgo(HallazgoDeAuditoria hallazgo) {
        if ("CERRADA".equals(this.estado)) {
            throw new IllegalStateException("No se pueden registrar hallazgos en una auditoría cerrada.");
        }
        this.hallazgos.add(hallazgo);
    }

    public void cerrar() {
        this.estado = "CERRADA";
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<HallazgoDeAuditoria> getHallazgos() {
        return hallazgos;
    }

    public void setHallazgos(List<HallazgoDeAuditoria> hallazgos) {
        this.hallazgos = hallazgos;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioIdResponsable() {
        return usuarioIdResponsable;
    }

    public void setUsuarioIdResponsable(Long usuarioIdResponsable) {
        this.usuarioIdResponsable = usuarioIdResponsable;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
