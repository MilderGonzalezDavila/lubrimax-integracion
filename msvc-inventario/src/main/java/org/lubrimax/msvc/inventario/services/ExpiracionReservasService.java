package org.lubrimax.msvc.inventario.services;

import org.lubrimax.msvc.inventario.repositories.ExistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ExpiracionReservasService {

    @Autowired private ExistenciaRepository existenciaRepository;

    @Transactional
    public void procesarExpiraciones() {

        LocalDateTime ahora = LocalDateTime.now();

        existenciaRepository
                .findAll()
                .forEach(
                        existencia -> {
                            int expiradas = existencia.expirarReservas(ahora);

                            if (expiradas > 0) {
                                existenciaRepository.save(existencia);
                            }
                        });
    }
}
