package com.chaudhrii.sterlingtechtask.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "starling")
@Data
public class StarlingProperties {
	private String starlingBaseUrl;
	private String bearerToken;
}
