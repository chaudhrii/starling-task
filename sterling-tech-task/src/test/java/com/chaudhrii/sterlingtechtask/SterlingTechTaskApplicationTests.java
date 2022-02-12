package com.chaudhrii.sterlingtechtask;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.chaudhrii.sterlingtechtask.controller.RoundUpSavingGoalController;

@SpringBootTest
class SterlingTechTaskApplicationTests {

	@Autowired
	private RoundUpSavingGoalController roundUpSavingGoalController;

	@Test
	void contextLoads() {
		assertNotNull(roundUpSavingGoalController);
	}
}
