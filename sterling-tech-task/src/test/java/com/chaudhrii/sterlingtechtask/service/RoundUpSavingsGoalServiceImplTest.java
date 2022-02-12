package com.chaudhrii.sterlingtechtask.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.chaudhrii.sterlingtechtask.sterling.api.CurrencyAndAmount;
import com.chaudhrii.sterlingtechtask.sterling.api.FeedItem;
import com.chaudhrii.sterlingtechtask.sterling.api.FeedItems;

class RoundUpSavingsGoalServiceImplTest {
	public static final String GBP = "GBP";

	@Test
	void whenRoundUp_andFeedItemsAreAsStarlingScenario() {
		// Given
		final var feedItem1 = FeedItem.outgoing(CurrencyAndAmount.of(GBP, 435));
		final var feedItem2 = FeedItem.outgoing(CurrencyAndAmount.of(GBP, 520));
		final var feedItem3 = FeedItem.outgoing(CurrencyAndAmount.of(GBP, 87));
		final var feedItems = new FeedItems(List.of(feedItem1, feedItem2, feedItem3));

		// When
		final var roundUpSum = RoundUpSavingsGoalServiceImpl.calculateRoundUpSum(feedItems);

		// Then
		assertEquals(158d, roundUpSum);
	}
}