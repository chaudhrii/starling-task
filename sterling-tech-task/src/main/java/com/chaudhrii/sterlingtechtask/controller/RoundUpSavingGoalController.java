package com.chaudhrii.sterlingtechtask.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chaudhrii.sterlingtechtask.api.RoundUpSavingsGoalRequest;
import com.chaudhrii.sterlingtechtask.service.RoundUpSavingsGoalServiceImpl;
import com.chaudhrii.sterlingtechtask.sterling.api.SavingsGoal;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RoundUpSavingGoalController {

	private RoundUpSavingsGoalServiceImpl service;

	public RoundUpSavingGoalController(final RoundUpSavingsGoalServiceImpl service) {
		this.service = service;
	}

	@PostMapping(
			path = "/account/{accountUid}/savings-goal/create-goal",
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<SavingsGoal> createRoundUpSavingsGoal(
			@PathVariable(name = "accountUid") final String accountUid,
			@RequestBody final RoundUpSavingsGoalRequest request
	) {
		log.info("Creating Round Up Savings Goal. accountUid: {}, request: {}", accountUid, request);
		final var savingsGoal = service.createRoundUpSavingsGoal(accountUid, request);
		return ResponseEntity.ok(savingsGoal); // probably should be 201
	}
}
