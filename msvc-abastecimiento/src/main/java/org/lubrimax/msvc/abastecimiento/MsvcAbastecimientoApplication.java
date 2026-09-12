package org.lubrimax.msvc.abastecimiento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsvcAbastecimientoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsvcAbastecimientoApplication.class, args);
    }
}
