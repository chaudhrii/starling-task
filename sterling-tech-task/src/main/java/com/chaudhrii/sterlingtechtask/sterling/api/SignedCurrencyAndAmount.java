package com.chaudhrii.sterlingtechtask.sterling.api;

import lombok.Data;

/**
 * Probably don't need this and {@link CurrencyAndAmount} but followed the API documentation in this regard
 */
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
