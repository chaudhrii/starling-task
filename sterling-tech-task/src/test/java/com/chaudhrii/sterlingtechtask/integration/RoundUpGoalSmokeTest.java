package com.chaudhrii.sterlingtechtask.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.Period;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.chaudhrii.sterlingtechtask.api.RoundUpSavingsGoalRequest;
import com.chaudhrii.sterlingtechtask.sterling.api.SavingsGoal;
import com.chaudhrii.sterlingtechtask.sterling.service.savingsgoal.StarlingGoalService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Note these tests utilise the Sterling Sandbox Environment.
 * <p>
 * These tests do not create accounts or bearer tokens.
 * If invoking this test it is a pre-requisite that you set these values in the application.yml file
 * <p>
 * The feedFromTime is a reference point from which to run the tests from
 * - When you generate data from the Sterling's Sandbox environment, you can use an appropriate UTC time here
 */

@SpringBootTest
@AutoConfigureMockMvc
class RoundUpGoalSmokeTest {
	@Autowired
	private MockMvc mockMvc;

	@Value("${smokeTest.gbpAccountUid}")
	private String gbpAccountUid;

	@Value("${smokeTest.gbpFeedFromTime}")
	private String gbpFeedFromTime;

	@Value("${starling.bearerToken}")
	private String bearerToken;

	@Autowired
	private StarlingGoalService goalService;

	private final ObjectMapper objectMapper = new ObjectMapper()
			.registerModule(new JavaTimeModule())
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
			.setSerializationInclusion(JsonInclude.Include.NON_NULL);

	//@Test
	void whenCreateGbpRoundUpSavingsGoal_thenRoundUpSavingsGoalIsCreated() throws Exception {
		final var request = RoundUpSavingsGoalRequest.ofDefaultPeriod(
				"IC Savings Goal",
				Instant.parse(gbpFeedFromTime)
		);

		final var requestJson = objectMapper.writeValueAsString(request);
		final var response = this.mockMvc.perform(
						post("/account/{accountUid}/savings-goal/create-goal", gbpAccountUid)
								.header("Authorization", "Bearer " + bearerToken)
								.content(requestJson)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
				).andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		final var savingsGoal = objectMapper.readValue(response.getResponse().getContentAsString(), SavingsGoal.class);

		try {
			assertNotNull(savingsGoal);
			assertNotNull(savingsGoal.getSavingsGoalUid());
		} finally {
			cleanUpSandbox(gbpAccountUid, savingsGoal); // In finally just in case something goes wrong in checks
		}
	}

	//@Test
	void whenCreateGbpRoundUpSavingsGoal_andDateInFuture() throws Exception {
		final var request = RoundUpSavingsGoalRequest.ofDefaultPeriod(
				"IC Savings Goal",
				Instant.now().plus(Period.ofDays(1))
		);
		final var requestJson = objectMapper.writeValueAsString(request);
		this.mockMvc.perform(
						post("/account/{accountUid}/savings-goal/create-goal", gbpAccountUid)
								.header("Authorization", "Bearer " + bearerToken)
								.content(requestJson)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
				).andDo(print())
				.andExpect(status().isNoContent())
				.andExpect(content().string(""));
	}

	private void cleanUpSandbox(final String accountUid, final SavingsGoal savingsGoal) throws InterruptedException {
		// A courtesy gap to allow Starling's Sandbox Env to persist the new savings goal
		Thread.sleep(2000);
		goalService.deleteSavingsGoal(accountUid, savingsGoal.getSavingsGoalUid());
	}
}
