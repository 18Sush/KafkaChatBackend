package com.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages={"com.proj.kafkachatserver.controllers","com.proj.kafkachatserver.services","com.proj.kafkachatserver.consumers"})
@EnableJpaRepositories("com.proj.kafkachatserver.repository")
@EntityScan("com.proj.kafkachatserver.models")
public class FinalChatServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinalChatServerApplication.class, args);
	}

}
