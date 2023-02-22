package com.krllrn.viewed;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ViewedApplication {

	public static final String LD_PATTERN = "yyyy-MM-dd";
	public static final String KP_URL = "https://www.kinopoisk.ru/film/";

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(ViewedApplication.class, args);
	}
}
