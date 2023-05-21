package ir.co.sadad.cartollapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Calendar;
import java.util.TimeZone;

@Slf4j
@EnableJpaAuditing
@SpringBootApplication
public class CarTollApiApplication {

	public static void main(String[] args) {
		log.warn("Jdk DST offset is: {}", Calendar.getInstance(TimeZone.getDefault()).get(Calendar.DST_OFFSET));
		log.warn("ICU4J DST offset is: {}", com.ibm.icu.util.Calendar.getInstance(com.ibm.icu.util.TimeZone.getDefault()).get(com.ibm.icu.util.Calendar.DST_OFFSET));
		SpringApplication.run(CarTollApiApplication.class, args);
	}

}
