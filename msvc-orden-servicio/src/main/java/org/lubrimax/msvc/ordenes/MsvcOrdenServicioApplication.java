package org.lubrimax.msvc.ordenes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsvcOrdenServicioApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsvcOrdenServicioApplication.class, args);
    }
}
