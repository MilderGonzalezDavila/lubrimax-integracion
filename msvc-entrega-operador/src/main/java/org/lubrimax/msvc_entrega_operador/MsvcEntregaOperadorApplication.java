package org.lubrimax.msvc_entrega_operador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsvcEntregaOperadorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsvcEntregaOperadorApplication.class, args);
    }
}
