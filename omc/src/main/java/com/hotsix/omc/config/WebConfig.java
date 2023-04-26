package com.hotsix.omc.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

	private static final int MAX_AGE = 1000 * 60 * 60;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		WebMvcConfigurer.super.addCorsMappings(registry);
		registry.addMapping("/**")
			.allowedOriginPatterns("*")
			.allowedOrigins("http://localhost:8081")
			.allowedOrigins("http://localhost:3000")
			.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
			.allowedHeaders("*")
			.allowCredentials(true)
			.maxAge(MAX_AGE);
	}
}
