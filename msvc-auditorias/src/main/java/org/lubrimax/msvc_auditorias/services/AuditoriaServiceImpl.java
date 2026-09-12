package org.lubrimax.msvc_auditorias.services;

import org.lubrimax.msvc_auditorias.models.entity.AuditoriaDeControlInterno;
import org.lubrimax.msvc_auditorias.models.entity.HallazgoDeAuditoria;
import org.lubrimax.msvc_auditorias.repositories.AuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuditoriaServiceImpl implements AuditoriaService {

    @Autowired private AuditoriaRepository repository;

    @Override
    @Transactional
    public AuditoriaDeControlInterno crearAuditoria(AuditoriaDeControlInterno auditoria) {
        return repository.save(auditoria);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuditoriaDeControlInterno> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public void registrarHallazgoExterno(Long auditoriaId, HallazgoDeAuditoria hallazgo) {
        AuditoriaDeControlInterno auditoria =
                repository
                        .findById(auditoriaId)
                        .orElseThrow(() -> new RuntimeException("Auditoría no encontrada"));

        // Validamos la regla interna de la entidad antes de integrarla al agregado[cite: 1]
        if (!hallazgo.validarJustificacion()) {
            throw new IllegalArgumentException("El hallazgo requiere una justificación válida.");
        }

        // El comando del agregado rechaza la operación si la auditoría está cerrada[cite: 1, 3]
        auditoria.registrarHallazgo(hallazgo);

        repository.save(auditoria);
    }

    @Override
    @Transactional
    public AuditoriaDeControlInterno cerrarAuditoria(Long auditoriaId) {
        AuditoriaDeControlInterno auditoria =
                repository
                        .findById(auditoriaId)
                        .orElseThrow(() -> new RuntimeException("Auditoría no encontrada"));

        auditoria.cerrar();
        return repository.save(auditoria);
    }
}
