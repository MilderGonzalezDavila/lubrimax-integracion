package org.lubrimax.msvc_politicas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsvcPoliticasApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsvcPoliticasApplication.class, args);
    }
}
