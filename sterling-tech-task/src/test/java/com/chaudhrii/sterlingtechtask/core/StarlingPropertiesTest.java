package com.chaudhrii.sterlingtechtask.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.chaudhrii.sterlingtechtask.core.config.StarlingProperties;

@SpringBootTest
class StarlingPropertiesTest {

	@Autowired
	private StarlingProperties starlingProperties;

	@Test
	void canReadBaseUrl() {
		assertEquals("https://api-sandbox.starlingbank-test.com", starlingProperties.getStarlingBaseUrl());
	}
}