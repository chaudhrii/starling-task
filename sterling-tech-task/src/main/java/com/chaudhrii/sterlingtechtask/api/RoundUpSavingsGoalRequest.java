package com.chaudhrii.sterlingtechtask.api;

import java.time.Instant;

import lombok.Data;

@Data
public class RoundUpSavingsGoalRequest {
	private String savingsGoalName;
	private Instant minTransactionTimestamp;
	private Instant maxTransactionTimestamp;

	public static RoundUpSavingsGoalRequest ofDefaultPeriod(final String savingsGoalName, final Instant minTransactionTimestamp) {
		final var request = new RoundUpSavingsGoalRequest();
		request.savingsGoalName = savingsGoalName;
		request.minTransactionTimestamp = minTransactionTimestamp;
		return request;
	}

	public static RoundUpSavingsGoalRequest ofPeriod(final String savingsGoalName, final Instant minTransactionTimestamp, final Instant maxTransactionTimestamp) {
		final var request = ofDefaultPeriod(savingsGoalName, minTransactionTimestamp);
		request.maxTransactionTimestamp = maxTransactionTimestamp;
		return request;
	}
}
