package org.lubrimax.msvc.inventario.services;

import org.lubrimax.msvc.inventario.models.MotivoDeAjuste;
import org.lubrimax.msvc.inventario.models.TipoMovimiento;
import org.lubrimax.msvc.inventario.models.entity.ExistenciaDeProducto;
import org.lubrimax.msvc.inventario.models.entity.MovimientoDeStock;
import org.lubrimax.msvc.inventario.models.entity.SaldoDeAlmacen;
import org.lubrimax.msvc.inventario.repositories.ExistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class InventarioServiceImpl implements InventarioService {

    @Autowired
    private ExistenciaRepository existenciaRepository;

    @Value("${inventario.reserva.duracion-minutos}")
    private long duracionReservaMinutos;
    @Override
    @Transactional(readOnly = true)
    public List<ExistenciaDeProducto> listar() {
        return  (List<ExistenciaDeProducto>) existenciaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExistenciaDeProducto> buscarPorId(Long id) {
        return existenciaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExistenciaDeProducto> buscarPorProductoPresentacion(Long productoId, Long presentacionId) {
        return existenciaRepository.findByProductoIdAndPresentacionId(productoId, presentacionId);
    }

    @Override
    @Transactional
    public ExistenciaDeProducto habilitarStock(Long productoId, Long presentacionId, BigDecimal cantidad, Long recepcionId) {

        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad a habilitar debe ser mayor que cero"
            );
        }

        if (productoId == null) {
            throw new IllegalArgumentException(
                    "El producto es obligatorio"
            );
        }

        if (presentacionId == null) {
            throw new IllegalArgumentException(
                    "La presentación es obligatoria"
            );
        }

        if (recepcionId == null) {
            throw new IllegalArgumentException(
                    "La recepción es obligatoria"
            );
        }

        ExistenciaDeProducto existencia = existenciaRepository
                .findByProductoIdAndPresentacionId(productoId, presentacionId)
                .orElseGet(() -> {
                    SaldoDeAlmacen saldoInicial = new SaldoDeAlmacen(BigDecimal.ZERO, BigDecimal.ZERO);
                    return new ExistenciaDeProducto(productoId, presentacionId, saldoInicial);
                });

        // 3. Evitar procesar dos veces la misma recepción
        boolean recepcionYaProcesada =
                existencia.getMovimientos().stream()
                        .anyMatch(movimiento ->
                                movimiento.getTipo()
                                        == TipoMovimiento.INGRESO
                                        && "RECEPCION".equals(
                                        movimiento.getOrigen()
                                )
                                        && Objects.equals(
                                        movimiento.getReferenciaOrigen(),
                                        recepcionId
                                )
                        );

        if (recepcionYaProcesada) {
            return existencia;
        }

        // Modificamos el saldo sumando la cantidad físicamente
        BigDecimal nuevoFisico = existencia.getSaldo().getFisico().add(cantidad);
        existencia.setSaldo(new SaldoDeAlmacen(nuevoFisico, existencia.getSaldo().getReservado()));

        // Registramos el movimiento obligatorio según el diagrama
        MovimientoDeStock movimiento = new MovimientoDeStock(
                TipoMovimiento.INGRESO,
                "RECEPCION",
                cantidad,
                MotivoDeAjuste.NINGUNO,
                recepcionId
        );
        existencia.getMovimientos().add(movimiento);

        return existenciaRepository.save(existencia);
    }

    @Override
    @Transactional
    public ExistenciaDeProducto reservar(
            Long existenciaId,
            Long ordenId,
            BigDecimal cantidad) {

        ExistenciaDeProducto existencia =
                existenciaRepository.findById(existenciaId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "No se encontró la existencia con ID: "
                                                + existenciaId
                                )
                        );

        LocalDateTime expiraEn =
                LocalDateTime.now()
                        .plusMinutes(duracionReservaMinutos);

        existencia.reservar(
                ordenId,
                cantidad,
                expiraEn
        );

        return existenciaRepository.save(existencia);
    }

    @Override
    @Transactional
    public ExistenciaDeProducto consumir(Long existenciaId, Long ordenId, BigDecimal cantidad) {
        if (ordenId == null) { throw new IllegalArgumentException( "La orden es obligatoria para consumir stock" ); }
        ExistenciaDeProducto existencia = existenciaRepository.findById(existenciaId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la existencia con ID: " + existenciaId));

        existencia.consumir(ordenId,cantidad);
        // Registramos el movimiento de salida
        MovimientoDeStock movimiento = new MovimientoDeStock(
                TipoMovimiento.SALIDA_CONSUMO,
                "VENTA_ORDEN",
                cantidad,
                MotivoDeAjuste.NINGUNO,
                ordenId
        );
        existencia.getMovimientos().add(movimiento);

        return existenciaRepository.save(existencia);
    }

    @Override
    @Transactional
    public ExistenciaDeProducto confirmarReserva(Long existenciaId, Long ordenId) {

        ExistenciaDeProducto existencia = existenciaRepository.findById(existenciaId)
                        .orElseThrow(() -> new IllegalArgumentException( "No se encontró la existencia con ID: " + existenciaId)
                        );

        existencia.confirmarReserva(ordenId);

        return existenciaRepository.save(existencia);
    }

    @Override
    @Transactional
    public ExistenciaDeProducto liberarReserva(Long existenciaId, Long ordenId) {
        ExistenciaDeProducto existencia = existenciaRepository.findById(existenciaId)
                        .orElseThrow(() -> new IllegalArgumentException( "No se encontró la existencia con ID: " + existenciaId));
        existencia.liberarReserva(ordenId);
        return existenciaRepository.save(existencia);
    }

    @Override
    @Transactional
    public void eliminarPorId(Long id) {
        existenciaRepository.deleteById(id);
    }

}
