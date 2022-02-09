package com.chaudhrii.sterlingtechtask.api;

import java.time.Instant;

import lombok.Data;

@Data
public class RoundUpSavingsGoalRequest {
	private String savingsGoalName;
	private Instant minTransactionTimestamp;
	private Instant maxTransactionTimestamp;
}
