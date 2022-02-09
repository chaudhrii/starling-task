package com.chaudhrii.sterlingtechtask.sterling.api;

import lombok.Data;

@Data
public class CurrencyAndAmount {
	private String currency;
	private long minorUnits;

	public static CurrencyAndAmount of(final String currency, final long minorUnits) {
		final var currencyAndAmount = new CurrencyAndAmount();
		currencyAndAmount.currency = currency;
		currencyAndAmount.minorUnits = minorUnits;
		return currencyAndAmount;
	}
}
