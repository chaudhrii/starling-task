package com.chaudhrii.sterlingtechtask.sterling.api;

import lombok.Data;

@Data
public class Balance {
	private SignedCurrencyAndAmount amount;

	public static Balance of(final SignedCurrencyAndAmount amount) {
		var balance = new Balance();
		balance.setAmount(amount);
		return balance;
	}
}
