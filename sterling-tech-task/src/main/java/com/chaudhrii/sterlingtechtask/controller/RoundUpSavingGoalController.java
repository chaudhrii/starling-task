package com.chaudhrii.sterlingtechtask.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
	private final RoundUpSavingsGoalServiceImpl service;

	public RoundUpSavingGoalController(final RoundUpSavingsGoalServiceImpl service) {
		this.service = service;
	}

	@PostMapping(
			path = "/account/{accountUid}/savings-goal/create-goal",
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<SavingsGoal> createRoundUpSavingsGoalOnAccount(
			@PathVariable(name = "accountUid") final String accountUid,
			@RequestBody final RoundUpSavingsGoalRequest request
	) {
		SavingsGoal savingsGoal;
		// TODO : Obfuscate or remove logging
		log.info("Creating Round Up Savings Goal. accountUid: {}, request: {}", accountUid, request);
		savingsGoal = service.createRoundUpSavingsGoal(accountUid, request);

		if (null == savingsGoal) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return ResponseEntity.ok(savingsGoal); // probably should be 201
	}
}
