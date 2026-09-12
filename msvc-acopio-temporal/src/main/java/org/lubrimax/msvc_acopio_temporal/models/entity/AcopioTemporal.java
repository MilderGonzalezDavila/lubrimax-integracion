package org.lubrimax.msvc_acopio_temporal.models.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import org.lubrimax.msvc_acopio_temporal.models.CantidadDeResiduo;
import org.lubrimax.msvc_acopio_temporal.models.CapacidadDeAcopio;
import org.lubrimax.msvc_acopio_temporal.models.CondicionDeAcopio;
import org.lubrimax.msvc_acopio_temporal.models.EstadoDelResiduo;
import org.lubrimax.msvc_acopio_temporal.models.TipoDeResiduo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "acopios_temporales")
public class AcopioTemporal {

    @Id
    @Enumerated(EnumType.STRING)
    private TipoDeResiduo tipoDeResiduo;

    @Version private int version;

    @Embedded private CapacidadDeAcopio capacidad;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "acopio_tipo_residuo", nullable = false)
    private List<ResiduoGenerado> residuos = new ArrayList<>();

    protected AcopioTemporal() {}

    public AcopioTemporal(TipoDeResiduo tipoDeResiduo, CapacidadDeAcopio capacidad) {
        if (tipoDeResiduo == null) {
            throw new IllegalArgumentException("El tipo de residuo es obligatorio");
        }
        if (capacidad == null) {
            throw new IllegalArgumentException("La capacidad del acopio es obligatoria");
        }
        this.tipoDeResiduo = tipoDeResiduo;
        this.capacidad = capacidad;
    }

    public ResiduoGenerado registrarGeneracion(
            Long ordenId,
            Long declaracionOrigenId,
            TipoDeResiduo tipo,
            CantidadDeResiduo cantidad,
            LocalDateTime fechaGeneracion) {
        if (tipo != tipoDeResiduo) {
            throw new IllegalArgumentException("El residuo no corresponde al tipo de este acopio");
        }
        ResiduoGenerado residuo =
                new ResiduoGenerado(ordenId, declaracionOrigenId, tipo, cantidad, fechaGeneracion);
        residuos.add(residuo);
        return residuo;
    }

    public void configurarCapacidad(CapacidadDeAcopio nuevaCapacidad) {
        if (nuevaCapacidad == null) {
            throw new IllegalArgumentException("La capacidad es obligatoria");
        }
        if (!nuevaCapacidad.getUnidad().equals(capacidad.getUnidad())) {
            throw new IllegalArgumentException(
                    "No se puede cambiar la unidad de un acopio existente");
        }
        if (nuevaCapacidad.getValorMaximo().compareTo(cantidadAcopiada()) < 0) {
            throw new IllegalArgumentException(
                    "La nueva capacidad es menor que la cantidad almacenada");
        }
        this.capacidad = nuevaCapacidad;
    }

    public void almacenar(Long residuoId) {
        ResiduoGenerado residuo = buscarResiduo(residuoId);
        if (!capacidad.admite(cantidadAcopiada(), residuo.getCantidad())) {
            throw new IllegalStateException("La cantidad supera la capacidad del acopio");
        }
        residuo.almacenar();
    }

    public void confirmarEntrega(Long entregaId, List<Long> residuosIds) {
        if (residuosIds == null || residuosIds.isEmpty()) {
            throw new IllegalArgumentException("Debe indicar al menos un residuo");
        }
        List<ResiduoGenerado> seleccionados =
                residuosIds.stream().distinct().map(this::buscarResiduo).toList();
        boolean todosYaEntregados =
                seleccionados.stream()
                        .allMatch(
                                residuo ->
                                        residuo.getEstado() == EstadoDelResiduo.ENTREGADO
                                                && entregaId.equals(residuo.getEntregaId()));
        if (todosYaEntregados) {
            return;
        }
        boolean algunoNoDisponible =
                seleccionados.stream()
                        .anyMatch(residuo -> residuo.getEstado() != EstadoDelResiduo.ALMACENADO);
        if (algunoNoDisponible) {
            throw new IllegalStateException("Todos los residuos deben estar ALMACENADOS");
        }
        seleccionados.forEach(residuo -> residuo.entregar(entregaId));
    }

    public BigDecimal cantidadAcopiada() {
        return residuos.stream()
                .filter(residuo -> residuo.getEstado() == EstadoDelResiduo.ALMACENADO)
                .map(residuo -> residuo.getCantidad().getValor())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal capacidadDisponible() {
        return capacidad.disponible(cantidadAcopiada());
    }

    public CondicionDeAcopio condicionActual() {
        return capacidad.condicion(cantidadAcopiada());
    }

    public ResiduoGenerado buscarResiduo(Long residuoId) {
        return residuos.stream()
                .filter(residuo -> residuo.getId().equals(residuoId))
                .findFirst()
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "No se encontro el residuo con ID: " + residuoId));
    }

    public TipoDeResiduo getTipoDeResiduo() {
        return tipoDeResiduo;
    }

    public CapacidadDeAcopio getCapacidad() {
        return capacidad;
    }

    public List<ResiduoGenerado> getResiduos() {
        return Collections.unmodifiableList(residuos);
    }
}
