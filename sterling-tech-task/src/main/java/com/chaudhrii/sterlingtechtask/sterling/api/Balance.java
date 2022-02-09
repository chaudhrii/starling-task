package com.chaudhrii.sterlingtechtask.sterling.api;

import lombok.Data;

@Data
public class Balance {
	private SignedCurrencyAndAmount clearedBalance;
	private SignedCurrencyAndAmount effectiveBalance;
	private SignedCurrencyAndAmount pendingTransactions;
	private SignedCurrencyAndAmount acceptedOverdraft;
	private SignedCurrencyAndAmount amount;
	private SignedCurrencyAndAmount totalClearedBalance;
	private SignedCurrencyAndAmount totalEffectiveBalance;
}
