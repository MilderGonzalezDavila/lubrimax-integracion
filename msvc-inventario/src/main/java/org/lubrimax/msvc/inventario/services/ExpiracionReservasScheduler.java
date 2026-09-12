package org.lubrimax.msvc.inventario.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExpiracionReservasScheduler {
    private final ExpiracionReservasService expiracionReservasService;

    public ExpiracionReservasScheduler(
            ExpiracionReservasService expiracionReservasService) {

        this.expiracionReservasService =
                expiracionReservasService;
    }

    @Scheduled(fixedRate = 60000)
    public void expirarReservas() {

        expiracionReservasService
                .procesarExpiraciones();
    }
}
