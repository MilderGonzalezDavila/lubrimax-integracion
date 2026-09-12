package org.lubrimax.msvc_ejecucion_mantenimiento.services;

import org.lubrimax.msvc_ejecucion_mantenimiento.clients.AcopioTemporalClienteRest;
import org.lubrimax.msvc_ejecucion_mantenimiento.clients.AgendaTecnicoClienteRest;
import org.lubrimax.msvc_ejecucion_mantenimiento.clients.OrdenServicioClienteRest;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.CantidadDeProducto;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.Kilometraje;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.LiquidacionDeServicio;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.ObservacionPreventiva;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.PrecioAplicado;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.ProductoAutorizado;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.ProgramacionDeProximoServicio;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.ServicioAutorizado;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.TipoDeResiduoGenerado;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.TipoDeTarea;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.VolumenDeclarado;
import org.lubrimax.msvc_ejecucion_mantenimiento.models.entity.EjecucionDeMantenimiento;
import org.lubrimax.msvc_ejecucion_mantenimiento.repositories.EjecucionDeMantenimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class EjecucionDeMantenimientoServiceImpl implements EjecucionDeMantenimientoService {

    private final EjecucionDeMantenimientoRepository repository;
    private final OrdenServicioClienteRest ordenCliente;
    private final AgendaTecnicoClienteRest agendaCliente;
    private final AcopioTemporalClienteRest acopioCliente;

    public EjecucionDeMantenimientoServiceImpl(EjecucionDeMantenimientoRepository repository,
                                                OrdenServicioClienteRest ordenCliente,
                                                AgendaTecnicoClienteRest agendaCliente,
                                                AcopioTemporalClienteRest acopioCliente) {
        this.repository = repository;
        this.ordenCliente = ordenCliente;
        this.agendaCliente = agendaCliente;
        this.acopioCliente = acopioCliente;
    }

    @Override
    @Transactional
    public EjecucionDeMantenimiento crear(Long ordenId, Long tecnicoUsuarioId, Long kilometraje) {
        if (repository.existsById(ordenId)) throw new IllegalArgumentException("Ya existe una ejecucion para la orden");
        var orden = ordenCliente.obtenerParaEjecucion(ordenId);
        if (!orden.autorizadaYEmitida()) throw new IllegalStateException("La orden no esta autorizada y emitida");
        if (!agendaCliente.validarAsignacion(tecnicoUsuarioId, ordenId).valida()) {
            throw new IllegalStateException("El tecnico no tiene una asignacion valida");
        }
        List<ServicioAutorizado> servicios = listaSegura(orden.servicios()).stream()
                .map(servicio -> new ServicioAutorizado(servicio.servicioId(), precio(servicio.precio()))).toList();
        List<ProductoAutorizado> productos = listaSegura(orden.productos()).stream()
                .map(producto -> new ProductoAutorizado(producto.productoId(), precio(producto.precio()))).toList();
        return repository.save(new EjecucionDeMantenimiento(ordenId, tecnicoUsuarioId,
                new Kilometraje(kilometraje), servicios, productos));
    }

    @Override
    @Transactional(readOnly = true)
    public EjecucionDeMantenimiento obtener(Long ordenId) {
        return repository.findById(ordenId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontro la ejecucion de la orden: " + ordenId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EjecucionDeMantenimiento> listar() { return (List<EjecucionDeMantenimiento>) repository.findAll(); }

    @Override
    @Transactional
    public EjecucionDeMantenimiento iniciar(Long ordenId) {
        EjecucionDeMantenimiento ejecucion = obtener(ordenId);
        var orden = ordenCliente.obtenerParaEjecucion(ordenId);
        boolean asignacionValida = agendaCliente.validarAsignacion(ejecucion.getTecnicoUsuarioId(), ordenId).valida();
        ejecucion.iniciar(orden.autorizadaYEmitida(), asignacionValida);
        return repository.save(ejecucion);
    }

    @Override
    @Transactional
    public EjecucionDeMantenimiento suspender(Long ordenId) {
        EjecucionDeMantenimiento ejecucion = obtener(ordenId);
        ejecucion.suspender();
        return repository.save(ejecucion);
    }

    @Override
    @Transactional
    public EjecucionDeMantenimiento registrarTarea(Long ordenId, Long servicioId, TipoDeTarea tipo) {
        EjecucionDeMantenimiento ejecucion = obtener(ordenId);
        ejecucion.registrarTarea(servicioId, tipo);
        return repository.save(ejecucion);
    }

    @Override
    @Transactional
    public EjecucionDeMantenimiento iniciarTarea(Long ordenId, Long tareaId) {
        EjecucionDeMantenimiento ejecucion = obtener(ordenId);
        ejecucion.iniciarTarea(tareaId);
        return repository.save(ejecucion);
    }

    @Override
    @Transactional
    public EjecucionDeMantenimiento completarTarea(Long ordenId, Long tareaId) {
        EjecucionDeMantenimiento ejecucion = obtener(ordenId);
        ejecucion.completarTarea(tareaId);
        return repository.save(ejecucion);
    }

    @Override
    @Transactional
    public EjecucionDeMantenimiento marcarTareaNoRealizada(Long ordenId, Long tareaId) {
        EjecucionDeMantenimiento ejecucion = obtener(ordenId);
        ejecucion.marcarTareaNoRealizada(tareaId);
        return repository.save(ejecucion);
    }

    @Override
    @Transactional
    public EjecucionDeMantenimiento registrarConsumo(Long ordenId, Long productoId, CantidadDeProducto cantidad) {
        EjecucionDeMantenimiento ejecucion = obtener(ordenId);
        ejecucion.registrarConsumo(productoId, cantidad);
        return repository.save(ejecucion);
    }

    @Override
    @Transactional
    public EjecucionDeMantenimiento declararResiduo(Long ordenId, Long tareaId,
                                                     TipoDeResiduoGenerado tipo, VolumenDeclarado volumen) {
        EjecucionDeMantenimiento ejecucion = obtener(ordenId);
        ejecucion.declararResiduo(tareaId, tipo, volumen);
        return repository.save(ejecucion);
    }

    @Override
    @Transactional
    public EjecucionDeMantenimiento registrarObservacion(Long ordenId, ObservacionPreventiva observacion) {
        EjecucionDeMantenimiento ejecucion = obtener(ordenId);
        ejecucion.registrarObservacion(observacion);
        return repository.save(ejecucion);
    }

    @Override
    @Transactional
    public LiquidacionDeServicio calcularLiquidacion(Long ordenId) {
        EjecucionDeMantenimiento ejecucion = obtener(ordenId);
        LiquidacionDeServicio liquidacion = ejecucion.calcularLiquidacion();
        repository.save(ejecucion);
        return liquidacion;
    }

    @Override
    @Transactional
    public EjecucionDeMantenimiento definirProximoServicio(Long ordenId,
                                                            ProgramacionDeProximoServicio programacion) {
        EjecucionDeMantenimiento ejecucion = obtener(ordenId);
        ejecucion.definirProximoServicio(programacion);
        return repository.save(ejecucion);
    }

    @Override
    @Transactional
    public EjecucionDeMantenimiento cerrar(Long ordenId) {
        EjecucionDeMantenimiento ejecucion = obtener(ordenId);
        ejecucion.validarCierre();
        ejecucion.getResiduos().forEach(residuo -> acopioCliente.registrarResiduo(residuo.getTipo().name(),
                new AcopioTemporalClienteRest.RegistrarResiduoRequest(ordenId, residuo.getId(),
                        residuo.getVolumen().getValor(), residuo.getVolumen().getUnidad(),
                        residuo.getMomentoDeclaracion())));
        ejecucion.cerrar();
        return repository.save(ejecucion);
    }

    private PrecioAplicado precio(OrdenServicioClienteRest.PrecioResponse precio) {
        if (precio == null) throw new IllegalArgumentException("Todo concepto autorizado debe tener precio");
        return new PrecioAplicado(precio.monto(), precio.moneda(), precio.momento());
    }

    private <T> List<T> listaSegura(List<T> lista) {
        return lista == null ? Collections.emptyList() : lista;
    }
}
