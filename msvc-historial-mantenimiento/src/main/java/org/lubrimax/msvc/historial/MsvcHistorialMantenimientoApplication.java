package org.lubrimax.msvc.historial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsvcHistorialMantenimientoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcHistorialMantenimientoApplication.class, args);
	}

}
