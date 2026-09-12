package org.lubrimax.msvc_auditorias.services;

import org.lubrimax.msvc_auditorias.models.entity.AuditoriaDeControlInterno;
import org.lubrimax.msvc_auditorias.models.entity.HallazgoDeAuditoria;

import java.util.Optional;

public interface AuditoriaService {
    AuditoriaDeControlInterno crearAuditoria(AuditoriaDeControlInterno auditoria);

    Optional<AuditoriaDeControlInterno> buscarPorId(Long id);

    void registrarHallazgoExterno(Long auditoriaId, HallazgoDeAuditoria hallazgo);

    AuditoriaDeControlInterno cerrarAuditoria(Long auditoriaId);
}
