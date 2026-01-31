package br.lawtrel.tecnomanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class TecnomanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TecnomanagerApplication.class, args);
	}

}
