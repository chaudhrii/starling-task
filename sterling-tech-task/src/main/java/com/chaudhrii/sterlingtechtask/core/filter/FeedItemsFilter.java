package com.chaudhrii.sterlingtechtask.core.filter;

import java.time.Instant;

import lombok.Data;

@Data
public class FeedItemsFilter {
	private String currency;
	private Instant fromTime;
	private Instant toTime;

	public static FeedItemsFilter of(final String currency, final Instant fromTime, final Instant toTime) {
		final var feedItemsFilter = new FeedItemsFilter();
		feedItemsFilter.currency = currency;
		feedItemsFilter.fromTime = fromTime;
		feedItemsFilter.toTime = toTime;
		return feedItemsFilter;
	}
}
