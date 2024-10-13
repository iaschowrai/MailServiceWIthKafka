package com.iaschowrai.kafka_mailservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KafkaMailserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaMailserviceApplication.class, args);
	}

}
