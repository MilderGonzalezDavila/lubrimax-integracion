package org.lubrimax.msvc_comprobante.msvc_comprobante;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsvcComprobanteApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsvcComprobanteApplication.class, args);
    }
}
