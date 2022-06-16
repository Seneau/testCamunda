package com.seneau;

import org.camunda.bpm.engine.RuntimeService;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
@EnableProcessApplication
@SpringBootApplication
public class MycongeCammundaApplication {



	@Autowired
	private RuntimeService runtimeService;


	@Bean
	public WebClient.Builder getWebClientBuilder(){
		return WebClient.builder();
	}
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(MycongeCammundaApplication.class, args);
	}

}
