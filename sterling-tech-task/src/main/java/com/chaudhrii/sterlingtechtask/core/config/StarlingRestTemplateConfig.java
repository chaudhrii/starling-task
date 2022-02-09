package com.chaudhrii.sterlingtechtask.core.config;

import java.util.List;

import org.springframework.boot.devtools.remote.client.HttpHeaderInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Configuration
public class StarlingRestTemplateConfig {

	private final StarlingProperties starlingProperties;

	public StarlingRestTemplateConfig(final StarlingProperties starlingProperties) {
		this.starlingProperties = starlingProperties;
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder
				.interceptors(List.of(
						new HttpHeaderInterceptor("Accept", MediaType.APPLICATION_JSON.toString()),
						new HttpHeaderInterceptor("Authorization", "Bearer " + starlingProperties.getBearerToken())
				))
				.build();
	}
}
