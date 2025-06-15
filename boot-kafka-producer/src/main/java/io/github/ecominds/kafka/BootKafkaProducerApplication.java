package io.github.ecominds.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class BootKafkaProducerApplication {
	public static void main(String[] args) {
		SpringApplication.run(BootKafkaProducerApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(){
		return args -> {
			log.info("Kafka producer is ready for service");
		};
	}
}
