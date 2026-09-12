package org.lubrimax.msvc_nota_credito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsvcNotaCreditoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsvcNotaCreditoApplication.class, args);
    }
}
