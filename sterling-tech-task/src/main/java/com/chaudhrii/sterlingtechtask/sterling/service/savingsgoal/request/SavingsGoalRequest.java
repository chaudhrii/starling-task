package com.chaudhrii.sterlingtechtask.sterling.service.savingsgoal.request;

import com.chaudhrii.sterlingtechtask.sterling.api.CurrencyAndAmount;

import lombok.Data;

@Data
public class SavingsGoalRequest {
	private String name;
	private String currency;
	private CurrencyAndAmount target;

	public static SavingsGoalRequest of(final String name, final String currency, final CurrencyAndAmount target) {
		final var sgr = new SavingsGoalRequest();
		sgr.name = name;
		sgr.currency = currency;
		sgr.target = target;
		return sgr;
	}
}
