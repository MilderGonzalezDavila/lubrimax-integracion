package org.lubrimax.msvc_ejecucion_mantenimiento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsvcEjecucionMantenimientoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsvcEjecucionMantenimientoApplication.class, args);
    }
}
