package org.lubrimax.msvc_operador_autorizado.services;

import org.lubrimax.msvc_operador_autorizado.models.AutorizacionDelOperador;
import org.lubrimax.msvc_operador_autorizado.models.Ruc;
import org.lubrimax.msvc_operador_autorizado.models.Telefono;
import org.lubrimax.msvc_operador_autorizado.models.entity.OperadorAutorizado;
import org.lubrimax.msvc_operador_autorizado.repositories.OperadorAutorizadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class OperadorAutorizadoServiceImpl implements OperadorAutorizadoService {

    private final OperadorAutorizadoRepository repository;

    public OperadorAutorizadoServiceImpl(OperadorAutorizadoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public OperadorAutorizado registrar(
            Ruc ruc, String razonSocial, Telefono telefono, AutorizacionDelOperador autorizacion) {
        validarDuplicados(null, ruc.getNumero(), autorizacion.getRegistroEors());
        return repository.save(new OperadorAutorizado(ruc, razonSocial, telefono, autorizacion));
    }

    @Override
    @Transactional(readOnly = true)
    public OperadorAutorizado buscarPorId(Long id) {
        return repository
                .findById(id)
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "No se encontro el operador con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OperadorAutorizado> listar() {
        return (List<OperadorAutorizado>) repository.findAll();
    }

    @Override
    @Transactional
    public OperadorAutorizado actualizarDatos(Long id, String razonSocial, Telefono telefono) {
        OperadorAutorizado operador = buscarPorId(id);
        operador.actualizarDatos(razonSocial, telefono);
        return repository.save(operador);
    }

    @Override
    @Transactional
    public OperadorAutorizado actualizarAutorizacion(
            Long id, AutorizacionDelOperador autorizacion) {
        OperadorAutorizado operador = buscarPorId(id);
        validarDuplicados(id, operador.getRuc().getNumero(), autorizacion.getRegistroEors());
        operador.actualizarAutorizacion(autorizacion);
        return repository.save(operador);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean consultarVigencia(Long id, LocalDate fecha) {
        return buscarPorId(id).estaVigenteAl(fecha);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OperadorAutorizado> listarVigentes(LocalDate fecha) {
        return repository
                .findByAutorizacionPeriodoDeVigenciaDesdeLessThanEqualAndAutorizacionPeriodoDeVigenciaHastaGreaterThanEqual(
                        fecha, fecha);
    }

    private void validarDuplicados(Long operadorId, String ruc, String registroEors) {
        repository
                .findByRucNumero(ruc)
                .ifPresent(
                        existente -> {
                            if (!existente.getId().equals(operadorId)) {
                                throw new IllegalArgumentException(
                                        "Ya existe un operador con el RUC indicado");
                            }
                        });
        repository
                .findByAutorizacionRegistroEors(registroEors)
                .ifPresent(
                        existente -> {
                            if (!existente.getId().equals(operadorId)) {
                                throw new IllegalArgumentException(
                                        "Ya existe un operador con el registro EORS indicado");
                            }
                        });
    }
}
