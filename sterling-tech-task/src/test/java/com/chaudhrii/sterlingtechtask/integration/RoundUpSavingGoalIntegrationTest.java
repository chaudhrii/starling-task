package com.chaudhrii.sterlingtechtask.integration;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.chaudhrii.sterlingtechtask.api.RoundUpSavingsGoalRequest;
import com.chaudhrii.sterlingtechtask.controller.RoundUpSavingGoalController;
import com.chaudhrii.sterlingtechtask.sterling.service.savingsgoal.StarlingGoalService;

import lombok.extern.slf4j.Slf4j;

/**
 * TODO IC MIGHT REMOVE
 */
@SpringBootTest
@Slf4j
class RoundUpSavingGoalIntegrationTest {

	@Autowired
	private RoundUpSavingGoalController controller;

	@Autowired
	private StarlingGoalService goalService;

	private static final String ACCOUNT_UID = "1db3f775-36e3-4e9f-bc9b-582cde60017f";

	//@Test
	void createRoundUpSavingsGoal() throws InterruptedException {

		final var request = new RoundUpSavingsGoalRequest();
		request.setSavingsGoalName("IC Savings Goal");
		request.setMinTransactionTimestamp(LocalDateTime.of(2022, 2, 5, 0, 0).toInstant(ZoneOffset.UTC));
		final var savingsGoal = controller.createRoundUpSavingsGoal(ACCOUNT_UID, request);
		Thread.sleep(2000);
		goalService.deleteSavingsGoal(ACCOUNT_UID, savingsGoal.getBody().getSavingsGoalUid());
	}


	void convenienceCleanUp() throws InterruptedException {
		final var savingsGoals = List.of(
				"a80ff46a-152d-4a40-8a8f-6ac61a194199",
				"af14f252-0763-4149-8c7a-755b4e3f5623",
				"be919209-d7d2-4faf-b476-c1babc49b6a2",
				"3deb76dd-7ba0-4fa8-8cbd-dba8369c8965",
				"a1937297-421b-4622-a3a6-d48725df5b20",
				"4ffaa2d0-537e-413d-8beb-8bcaa1b7f0bb",
				"f55bdaa2-824a-4aa4-bbd9-fcbaf3fd955e",
				"f2638d9b-ba78-406b-a762-24386bfb3c7d",
				"6ba4c6b4-1abe-4e2f-a571-e865f58eaf63",
				"856a6c81-05b1-400f-a1fe-951ad98629ed",
				"f91c77f7-ddaa-46ff-ba13-602040dde18b",
				"d9f3840b-e6dd-498c-9e9b-9d7e3607e574"
		);


		for (final String savingsGoal : savingsGoals) {
			goalService.deleteSavingsGoal(ACCOUNT_UID, savingsGoal);
			Thread.sleep(2000);
		}
	}
}
