package org.lubrimax.msvc_acopio_temporal.models;

import org.junit.jupiter.api.Test;
import org.lubrimax.msvc_acopio_temporal.models.entity.AcopioTemporal;
import org.lubrimax.msvc_acopio_temporal.models.entity.ResiduoGenerado;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AcopioTemporalTest {

    @Test
    void residuoConservaOrdenOrigen() {
        ResiduoGenerado residuo = acopio().registrarGeneracion(10L, 1L, TipoDeResiduo.ACEITE_USADO,
                cantidad("10"), LocalDateTime.now());
        assertEquals(10L, residuo.getOrdenId());
    }

    @Test
    void transicionGeneradoAlmacenadoValida() {
        AcopioTemporal acopio = acopio();
        ResiduoGenerado residuo = acopio.registrarGeneracion(10L, 1L, TipoDeResiduo.ACEITE_USADO,
                cantidad("10"), LocalDateTime.now());
        asignarId(residuo, 1L);
        acopio.almacenar(1L);
        assertEquals(EstadoDelResiduo.ALMACENADO, residuo.getEstado());
    }

    @Test
    void noSaltaDeGeneradoAEntregado() {
        ResiduoGenerado residuo = new ResiduoGenerado(10L, 1L, TipoDeResiduo.ACEITE_USADO,
                cantidad("10"), LocalDateTime.now());
        assertThrows(IllegalStateException.class, () -> residuo.entregar(1L));
    }

    @Test
    void noPermiteRetroceso() {
        ResiduoGenerado residuo = new ResiduoGenerado(10L, 1L, TipoDeResiduo.ACEITE_USADO,
                cantidad("10"), LocalDateTime.now());
        residuo.almacenar();
        assertThrows(IllegalStateException.class, residuo::almacenar);
    }

    @Test
    void noMezclaTipos() {
        assertThrows(IllegalArgumentException.class, () -> acopio().registrarGeneracion(10L, 1L,
                TipoDeResiduo.FILTRO_USADO, cantidad("1"), LocalDateTime.now()));
    }

    @Test
    void noSuperaCapacidad() {
        AcopioTemporal acopio = acopio();
        ResiduoGenerado residuo = acopio.registrarGeneracion(10L, 1L, TipoDeResiduo.ACEITE_USADO,
                cantidad("101"), LocalDateTime.now());
        asignarId(residuo, 1L);
        assertThrows(IllegalStateException.class, () -> acopio.almacenar(1L));
    }

    @Test
    void noExisteEstadoEliminado() {
        assertFalse(List.of(EstadoDelResiduo.values()).stream().anyMatch(e -> e.name().equals("ELIMINADO")));
    }

    private AcopioTemporal acopio() {
        return new AcopioTemporal(TipoDeResiduo.ACEITE_USADO,
                new CapacidadDeAcopio(new BigDecimal("100"), "LITRO"));
    }

    private CantidadDeResiduo cantidad(String valor) {
        return new CantidadDeResiduo(new BigDecimal(valor), "LITRO");
    }

    private void asignarId(ResiduoGenerado residuo, Long id) {
        try {
            var campo = ResiduoGenerado.class.getDeclaredField("id");
            campo.setAccessible(true);
            campo.set(residuo, id);
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError(exception);
        }
    }
}
