package com.chaudhrii.sterlingtechtask.sterling.service.savingsgoal.response;

import lombok.Data;

@Data
public class SavingsGoalResponse {
	private String savingsGoalUid;
	private boolean success;

	public static SavingsGoalResponse of(final String savingsGoalUid, final boolean success) {
		final var sgr = new SavingsGoalResponse();
		sgr.savingsGoalUid = savingsGoalUid;
		sgr.success = success;
		return sgr;
	}
}
