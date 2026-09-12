package org.lubrimax.msvc.proveedor.services;

import org.lubrimax.msvc.proveedor.clients.AbastecimientoClienteRest;
import org.lubrimax.msvc.proveedor.models.entity.Proveedor;
import org.lubrimax.msvc.proveedor.repositories.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private AbastecimientoClienteRest abastecimientoClienteRest;

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> listarProveedores() {
        return (List<Proveedor>) proveedorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Proveedor> buscarProveedorPorId(Long id) {
        return proveedorRepository.findById(id);
    }

    @Override
    @Transactional
    public Proveedor registrarProveedor(Proveedor proveedor) {
        Optional<Proveedor> existente = proveedorRepository.findByRuc(proveedor.getRuc());
        if (existente.isPresent() && !existente.get().getId().equals(proveedor.getId())) {
            throw new IllegalArgumentException("Ya existe un proveedor registrado con el RUC: " + proveedor.getRuc());
        }
        return proveedorRepository.save(proveedor);
    }

    @Override
    @Transactional
    public void eliminarProveedorPorId(Long id) {
        if (abastecimientoClienteRest.existePorProveedorId(id)) {
            throw new IllegalStateException("No se puede eliminar el proveedor porque ya cuenta con recepciones de mercancía registradas en el historial.");
        }
        proveedorRepository.deleteById(id);
    }
}
