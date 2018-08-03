package net.sup2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MySup2Application {

	public static void main(String[] args) {
		SpringApplication.run(MySup2Application.class, args);
	}
}
