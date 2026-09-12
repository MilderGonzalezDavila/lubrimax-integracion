package org.lubrimax.msvc_lista_precio_base.services;

import org.lubrimax.msvc_lista_precio_base.models.PeriodoDeVigencia;
import org.lubrimax.msvc_lista_precio_base.models.TipoDeReferencia;
import org.lubrimax.msvc_lista_precio_base.models.entity.ListaDePreciosBase;
import org.lubrimax.msvc_lista_precio_base.models.entity.PrecioBase;
import org.lubrimax.msvc_lista_precio_base.repositories.ListaDePreciosBaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ListaDePreciosBaseServiceImpl implements ListaDePreciosBaseService {

    private final ListaDePreciosBaseRepository repository;

    public ListaDePreciosBaseServiceImpl(ListaDePreciosBaseRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListaDePreciosBase> listar() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ListaDePreciosBase obtener(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lista no encontrada"));
    }

    @Override
    @Transactional
    public ListaDePreciosBase crear(ListaDePreciosBase lista) {
        if (lista.getVigencia() == null || lista.getVigencia().getDesde() == null) {
            throw new IllegalArgumentException("La vigencia es obligatoria");
        }
        lista.setVigente(false);
        return repository.save(lista);
    }

    @Override
    @Transactional
    public ListaDePreciosBase agregarPrecio(Long id, PrecioBase precio) {
        ListaDePreciosBase lista = obtener(id);
        if (lista.isVigente()) {
            throw new IllegalStateException("No se modifica una lista vigente");
        }
        lista.getPrecios().add(precio);
        return repository.save(lista);
    }

    @Override
    @Transactional
    public ListaDePreciosBase activar(Long id) {
        ListaDePreciosBase lista = obtener(id);
        if (lista.getPrecios().isEmpty()) {
            throw new IllegalStateException("La lista requiere precios");
        }
        repository
                .findByVigenteTrue()
                .filter(anterior -> !anterior.getId().equals(id))
                .ifPresent(
                        anterior -> {
                            anterior.cerrar();
                            repository.save(anterior);
                        });
        lista.activar();
        return repository.save(lista);
    }

    @Override
    @Transactional
    public ListaDePreciosBase cerrar(Long id) {
        ListaDePreciosBase lista = obtener(id);
        lista.cerrar();
        return repository.save(lista);
    }

    @Override
    @Transactional(readOnly = true)
    public PrecioBase precioVigente(TipoDeReferencia tipo, Long referenciaId) {
        ListaDePreciosBase lista =
                repository
                        .findByVigenteTrue()
                        .orElseThrow(() -> new IllegalStateException("No existe lista vigente"));
        if (!lista.getVigencia().estaVigente(LocalDateTime.now())) {
            throw new IllegalStateException("La lista activa esta fuera de vigencia");
        }
        return lista.getPrecios().stream()
                .filter(
                        p ->
                                p.getTipoReferencia() == tipo
                                        && p.getReferenciaId().equals(referenciaId))
                .findFirst()
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "La referencia no tiene precio vigente"));
    }
}
