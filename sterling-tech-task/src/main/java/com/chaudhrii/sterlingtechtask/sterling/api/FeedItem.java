package com.chaudhrii.sterlingtechtask.sterling.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedItem {
	private CurrencyAndAmount amount;
	private String direction;

	public static FeedItem outgoing(final CurrencyAndAmount amount) {
		final var fi = new FeedItem();
		fi.setDirection("OUT");
		fi.setAmount(amount);
		return fi;
	}
}
