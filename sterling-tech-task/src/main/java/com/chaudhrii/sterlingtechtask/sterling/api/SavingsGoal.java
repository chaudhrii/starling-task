package com.chaudhrii.sterlingtechtask.sterling.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SavingsGoal {
	private String savingsGoalUid;

	public static SavingsGoal of(final String savingsGoalUid) {
		final var sg = new SavingsGoal();
		sg.savingsGoalUid = savingsGoalUid;
		return sg;
	}
}
