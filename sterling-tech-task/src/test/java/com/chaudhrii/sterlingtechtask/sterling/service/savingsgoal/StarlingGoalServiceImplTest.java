package com.chaudhrii.sterlingtechtask.sterling.service.savingsgoal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.chaudhrii.sterlingtechtask.core.config.StarlingProperties;
import com.chaudhrii.sterlingtechtask.core.exception.StarlingException;
import com.chaudhrii.sterlingtechtask.sterling.api.CurrencyAndAmount;
import com.chaudhrii.sterlingtechtask.sterling.api.SavingsGoal;
import com.chaudhrii.sterlingtechtask.sterling.api.SavingsGoalList;
import com.chaudhrii.sterlingtechtask.sterling.api.error.ErrorResponse;
import com.chaudhrii.sterlingtechtask.sterling.service.savingsgoal.request.SavingsGoalRequest;
import com.chaudhrii.sterlingtechtask.sterling.service.savingsgoal.response.SavingsGoalResponse;

@ExtendWith(MockitoExtension.class)
class StarlingGoalServiceImplTest {
	private static final String GBP = "GBP";
	private static final String ACCOUNT_UID = "1db3f775-36e3-4e9f-bc9b-582cde60017f";
	private static final String SAVINGS_GOAL_UID = "90617b3a-7bef-4f1b-b2a3-91f5a1d3ee03";

	private static final SavingsGoal TEST_SAVINGS_GOAL = SavingsGoal.of(SAVINGS_GOAL_UID);
	private static final SavingsGoalRequest SAVINGS_GOAL_REQUEST = SavingsGoalRequest.of("Test Savings Goal", GBP, CurrencyAndAmount.of(GBP, 10000L));
	private static final SavingsGoalResponse SAVINGS_GOAL_RESPONSE = SavingsGoalResponse.of(SAVINGS_GOAL_UID, true);

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private StarlingProperties starlingProperties;

	@InjectMocks
	private StarlingGoalServiceImpl service;

	@Test
	void whenGetAllSavingsGoals_thenAllSavingsGoalsAreReturned() {
		// Given
		final var response = new ResponseEntity<>(new SavingsGoalList(List.of(TEST_SAVINGS_GOAL)), HttpStatus.OK);
		when(restTemplate.getForEntity(anyString(), eq(SavingsGoalList.class), eq(ACCOUNT_UID))).thenReturn(response);

		// When
		final var savingsGoals = service.getSavingsGoals(ACCOUNT_UID);

		// Then
		assertNotNull(savingsGoals);
		assertEquals(1, savingsGoals.getSavingsGoals().size());
		assertEquals(TEST_SAVINGS_GOAL, savingsGoals.getSavingsGoals().get(0));
	}

	@Test
	void whenGetAllSavingsGoals_andSavingsGoalsAreNotFound() {
		// Given
		final var error = new ErrorResponse();
		final var response = new ResponseEntity<>((Object) error, HttpStatus.NOT_FOUND);
		when(restTemplate.getForEntity(anyString(), any(), anyString())).thenReturn(response);

		// When/ Then
		final var ex = assertThrows(StarlingException.class, () -> service.getSavingsGoals(ACCOUNT_UID));
		assertEquals("Failed in Getting All Savings Goal Records from Starling", ex.getMessage());
	}

	@Test
	void whenGetSavingsGoalByUid_andSavingsGoalIsReturned() {
		// Given
		final var response = new ResponseEntity<>(TEST_SAVINGS_GOAL, HttpStatus.OK);
		when(restTemplate.getForEntity(anyString(), eq(SavingsGoal.class), eq(ACCOUNT_UID), eq(SAVINGS_GOAL_UID))).thenReturn(response);

		// When
		final var savingsGoal = service.getSavingsGoal(ACCOUNT_UID, SAVINGS_GOAL_UID);

		// Then
		assertNotNull(savingsGoal);
		assertEquals(TEST_SAVINGS_GOAL, savingsGoal);
	}

	@Test
	void whenGetSavingsGoalByUid_andSavingsGoalIsNotFound() {
		// Given
		final var error = new ErrorResponse();
		final var response = new ResponseEntity<>((Object) error, HttpStatus.NOT_FOUND);
		when(restTemplate.getForEntity(anyString(), any(), eq(ACCOUNT_UID), eq(SAVINGS_GOAL_UID))).thenReturn(response);

		// When / Then
		final var ex = assertThrows(StarlingException.class, () -> service.getSavingsGoal(ACCOUNT_UID, SAVINGS_GOAL_UID));
		assertEquals(String.format("Failed in Getting Savings Goal from Starling for accountUid:%s and savingsGoalUid:%s", ACCOUNT_UID, SAVINGS_GOAL_UID), ex.getMessage());
	}

	@Test
	void whenCreateSavingsGoal_andSavingsGoalIsCreated() {
		// Given
		final var response = new ResponseEntity<>(SAVINGS_GOAL_RESPONSE, HttpStatus.OK);
		when(restTemplate.exchange(any(), eq(SavingsGoalResponse.class))).thenReturn(response);

		// When
		final var savingsGoal = service.createSavingsGoal(ACCOUNT_UID, SAVINGS_GOAL_REQUEST);

		// Then
		assertNotNull(savingsGoal);
		assertEquals(TEST_SAVINGS_GOAL, savingsGoal);
	}

	@Test
	void whenCreateSavingsGoal_andSavingsGoalIsNotCreated() {
		// Given
		final var error = new ErrorResponse();
		final ResponseEntity<?> response = new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

		// TODO: IC: Not happy with the unchecked assignment, deemed low-risk proceeding given time constraints
		when(restTemplate.exchange(any(), any(Class.class))).thenReturn(response);

		// When/ Then
		final var ex = assertThrows(StarlingException.class, () -> service.createSavingsGoal(ACCOUNT_UID, SAVINGS_GOAL_REQUEST));
		assertEquals(String.format("Failed in Creating Savings Goal at Starling for accountUid:%s and Savings Goal Name:%s", ACCOUNT_UID, SAVINGS_GOAL_REQUEST.getName()), ex.getMessage());
	}

	@Test
	void whenDeleteSavingsGoal_andSavingsGoalIsDeleted() {
		assertDoesNotThrow(() -> service.deleteSavingsGoal(ACCOUNT_UID, SAVINGS_GOAL_UID));
	}
}