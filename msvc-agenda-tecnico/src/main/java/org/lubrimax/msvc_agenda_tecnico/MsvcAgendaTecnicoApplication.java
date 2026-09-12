package org.lubrimax.msvc_agenda_tecnico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsvcAgendaTecnicoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcAgendaTecnicoApplication.class, args);
	}

}
