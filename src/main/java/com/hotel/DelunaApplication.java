package com.hotel;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableScheduling
@SpringBootApplication
public class DelunaApplication implements WebMvcConfigurer{

	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/member/sign-in").allowedOrigins("*");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(DelunaApplication.class, args);
	}

}
