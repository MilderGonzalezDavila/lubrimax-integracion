package org.lubrimax.msvc_entrega_operador.services;

import org.lubrimax.msvc_entrega_operador.clients.AcopioTemporalClienteRest;
import org.lubrimax.msvc_entrega_operador.clients.OperadorAutorizadoClienteRest;
import org.lubrimax.msvc_entrega_operador.models.AutorizacionDelOperador;
import org.lubrimax.msvc_entrega_operador.models.CantidadDeResiduo;
import org.lubrimax.msvc_entrega_operador.models.EstadoDeEntrega;
import org.lubrimax.msvc_entrega_operador.models.ManifiestoDeResiduos;
import org.lubrimax.msvc_entrega_operador.models.TipoDeResiduo;
import org.lubrimax.msvc_entrega_operador.models.entity.EntregaAOperador;
import org.lubrimax.msvc_entrega_operador.models.entity.LineaDeEntrega;
import org.lubrimax.msvc_entrega_operador.repositories.EntregaAOperadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class EntregaAOperadorServiceImpl implements EntregaAOperadorService {

    private final EntregaAOperadorRepository repository;
    private final OperadorAutorizadoClienteRest operadorCliente;
    private final AcopioTemporalClienteRest acopioCliente;

    public EntregaAOperadorServiceImpl(EntregaAOperadorRepository repository,
                                       OperadorAutorizadoClienteRest operadorCliente,
                                       AcopioTemporalClienteRest acopioCliente) {
        this.repository = repository;
        this.operadorCliente = operadorCliente;
        this.acopioCliente = acopioCliente;
    }

    @Override
    @Transactional
    public EntregaAOperador programar(Long operadorId, Long usuarioId, LocalDate fechaEntrega) {
        return repository.save(new EntregaAOperador(operadorId, usuarioId, fechaEntrega));
    }

    @Override
    @Transactional(readOnly = true)
    public EntregaAOperador obtener(Long entregaId) {
        return repository.findById(entregaId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontro la entrega con ID: " + entregaId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntregaAOperador> listar() {
        return (List<EntregaAOperador>) repository.findAll();
    }

    @Override
    @Transactional
    public EntregaAOperador agregarLinea(Long entregaId, TipoDeResiduo tipo,
                                         CantidadDeResiduo cantidad, List<Long> residuosIds) {
        EntregaAOperador entrega = obtener(entregaId);
        entrega.agregarLinea(tipo, cantidad, residuosIds);
        return repository.save(entrega);
    }

    @Override
    @Transactional
    public EntregaAOperador eliminarLinea(Long entregaId, Long lineaId) {
        EntregaAOperador entrega = obtener(entregaId);
        entrega.eliminarLinea(lineaId);
        return repository.save(entrega);
    }

    @Override
    @Transactional
    public EntregaAOperador registrarManifiesto(Long entregaId, ManifiestoDeResiduos manifiesto) {
        EntregaAOperador entrega = obtener(entregaId);
        entrega.registrarManifiesto(manifiesto);
        return repository.save(entrega);
    }

    @Override
    @Transactional
    public EntregaAOperador ejecutar(Long entregaId) {
        EntregaAOperador entrega = obtener(entregaId);
        var vigencia = operadorCliente.consultarVigencia(entrega.getOperadorId(), entrega.getFechaEntrega());
        if (!vigencia.vigente()) throw new IllegalStateException("El operador no esta vigente en la fecha de entrega");
        entrega.getLineas().forEach(this::validarResiduosDisponibles);
        entrega.ejecutar(new AutorizacionDelOperador(vigencia.ruc(), vigencia.razonSocial(),
                vigencia.registroEors(), vigencia.desde(), vigencia.hasta()));
        return repository.save(entrega);
    }

    @Override
    @Transactional
    public EntregaAOperador conformar(Long entregaId) {
        EntregaAOperador entrega = obtener(entregaId);
        if (entrega.getEstado() == EstadoDeEntrega.CONFORMADA) return entrega;
        if (entrega.getEstado() != EstadoDeEntrega.EJECUTADA) {
            throw new IllegalStateException("Solo una entrega EJECUTADA puede conformarse");
        }
        entrega.getLineas().forEach(linea -> acopioCliente.confirmarEntrega(linea.getTipoDeResiduo().name(),
                new AcopioTemporalClienteRest.ConfirmarEntregaRequest(entregaId, linea.getResiduosIds())));
        entrega.conformar();
        return repository.save(entrega);
    }

    private void validarResiduosDisponibles(LineaDeEntrega linea) {
        List<AcopioTemporalClienteRest.ResiduoResponse> seleccionados = acopioCliente
                .listarResiduos(linea.getTipoDeResiduo().name()).stream()
                .filter(residuo -> linea.getResiduosIds().contains(residuo.id()))
                .toList();
        if (seleccionados.size() != linea.getResiduosIds().size()
                || seleccionados.stream().anyMatch(residuo -> !"ALMACENADO".equals(residuo.estado()))) {
            throw new IllegalStateException("Todos los residuos de la linea deben estar ALMACENADOS");
        }
        if (seleccionados.stream().anyMatch(residuo ->
                !linea.getCantidad().getUnidad().equalsIgnoreCase(residuo.cantidad().unidad()))) {
            throw new IllegalStateException("Las unidades de los residuos no coinciden con la linea");
        }
        BigDecimal disponible = seleccionados.stream().map(residuo -> residuo.cantidad().valor())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (disponible.compareTo(linea.getCantidad().getValor()) != 0) {
            throw new IllegalStateException("La cantidad de la linea debe coincidir con los residuos seleccionados");
        }
    }
}
