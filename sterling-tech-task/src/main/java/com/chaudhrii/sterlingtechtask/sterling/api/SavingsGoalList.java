package com.chaudhrii.sterlingtechtask.sterling.api;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SavingsGoalList {
	private List<SavingsGoal> savingsGoals;
}
