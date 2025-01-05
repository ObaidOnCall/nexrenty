package com.nexrenty.clients_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

import com.nexrenty.clients_service.config.ClientsServiceHintsConfig;

@SpringBootApplication
@ImportRuntimeHints(ClientsServiceHintsConfig.class)
public class ClientsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientsServiceApplication.class, args);
	}

}
