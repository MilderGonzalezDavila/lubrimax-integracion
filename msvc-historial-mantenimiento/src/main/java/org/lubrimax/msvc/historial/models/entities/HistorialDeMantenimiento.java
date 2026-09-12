package org.lubrimax.msvc.historial.models.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "historiales_mantenimiento")
public class HistorialDeMantenimiento {

    @Id private Long vehiculoId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private List<RegistroMantenimiento> registros = new ArrayList<>();

    public HistorialDeMantenimiento() {}

    public HistorialDeMantenimiento(Long vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    public void agregarRegistro(RegistroMantenimiento registro) {
        if (!registro.correspondeAServicioCerrado()) {
            throw new IllegalArgumentException(
                    "Solo se incorporan al historial servicios cerrados");
        }
        if (!registro.tieneProximoServicioDefinido()) {
            throw new IllegalArgumentException("Todo registro debe definir el próximo servicio");
        }

        ultimoRegistro()
                .ifPresent(
                        ultimo -> {
                            boolean fechaNoPosterior =
                                    !registro.getFechaAtencion().isAfter(ultimo.getFechaAtencion());
                            boolean kilometrajeNoPosterior =
                                    registro.getKilometraje() < ultimo.getKilometraje();
                            if (fechaNoPosterior || kilometrajeNoPosterior) {
                                throw new IllegalArgumentException(
                                        "Los registros deben ser crecientes en fecha y kilometraje");
                            }
                        });

        registros.add(registro);
    }

    public java.util.Optional<RegistroMantenimiento> ultimoRegistro() {
        return registros.stream()
                .max(Comparator.comparing(RegistroMantenimiento::getFechaAtencion));
    }

    public Long getVehiculoId() {
        return vehiculoId;
    }

    public void setVehiculoId(Long vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    public List<RegistroMantenimiento> getRegistros() {
        return registros;
    }

    public void setRegistros(List<RegistroMantenimiento> registros) {
        this.registros = registros;
    }
}
