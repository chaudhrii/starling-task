package com.chaudhrii.sterlingtechtask.sterling.api;

import lombok.Data;

@Data
public class SignedCurrencyAndAmount {
	private String currency;
	private long minorUnits;

	public static SignedCurrencyAndAmount of(final String currency, final long minorUnits) {
		final var signedCurrencyAndAmount = new SignedCurrencyAndAmount();
		signedCurrencyAndAmount.currency = currency;
		signedCurrencyAndAmount.minorUnits = minorUnits;
		return signedCurrencyAndAmount;
	}
}
