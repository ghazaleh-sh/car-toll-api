package ir.co.sadad.cartollapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CarTollApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarTollApiApplication.class, args);
	}

}
