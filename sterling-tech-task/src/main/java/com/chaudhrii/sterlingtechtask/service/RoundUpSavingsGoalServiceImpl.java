package com.chaudhrii.sterlingtechtask.service;

import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.chaudhrii.sterlingtechtask.api.RoundUpSavingsGoalRequest;
import com.chaudhrii.sterlingtechtask.core.config.StarlingProperties;
import com.chaudhrii.sterlingtechtask.core.filter.FeedItemsFilter;
import com.chaudhrii.sterlingtechtask.sterling.api.CurrencyAndAmount;
import com.chaudhrii.sterlingtechtask.sterling.api.FeedItem;
import com.chaudhrii.sterlingtechtask.sterling.api.FeedItems;
import com.chaudhrii.sterlingtechtask.sterling.api.SavingsGoal;
import com.chaudhrii.sterlingtechtask.sterling.service.account.StarlingAccountsService;
import com.chaudhrii.sterlingtechtask.sterling.service.savingsgoal.StarlingGoalService;
import com.chaudhrii.sterlingtechtask.sterling.service.savingsgoal.request.SavingsGoalRequest;
import com.chaudhrii.sterlingtechtask.sterling.service.transaction.StarlingFeedService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RoundUpSavingsGoalServiceImpl {
	private StarlingAccountsService accountsService;
	private StarlingFeedService feedService;
	private StarlingGoalService goalService;
	private StarlingProperties starlingProperties;

	public RoundUpSavingsGoalServiceImpl(
			final StarlingAccountsService accountsService,
			final StarlingFeedService feedService,
			final StarlingGoalService goalService,
			final StarlingProperties starlingProperties) {
		this.accountsService = accountsService;
		this.feedService = feedService;
		this.goalService = goalService;
		this.starlingProperties = starlingProperties;
	}

	public SavingsGoal createRoundUpSavingsGoal(final String accountUid, final RoundUpSavingsGoalRequest request) {
		final var balance = accountsService.getAccountBalance(accountUid);
		validate(request);

		final var accountCurrency = balance.getAmount().getCurrency();
		log.debug("Considering Currency: {}", accountCurrency);

		final var fromTime = request.getMinTransactionTimestamp();
		final var toTime = calculateToTime(request, fromTime);
		log.debug("Considered period in days: {}", calculateTimeDifferenceInDays(fromTime, toTime));

		final var feedItems= getOutgoingsFeedItemsInPeriod(accountUid, FeedItemsFilter.of(accountCurrency, fromTime, toTime));
		log.debug("Considering {} Outgoing feed items", feedItems.getFeedItems().size());

		// Round up sum
		final var roundUpSum = calculateRoundUpSum(feedItems);
		log.debug("Round Up sum {}", roundUpSum);

		if (roundUpSum == 0L) {
			log.info("Round up sum is zero. No Rule Created");
			return null;
		}

		// Create savings goal
		final var savingsGoalRequest = SavingsGoalRequest.of(request.getSavingsGoalName(), accountCurrency, CurrencyAndAmount.of(accountCurrency, roundUpSum));
		final var savingsGoal = goalService.createSavingsGoal(accountUid, savingsGoalRequest);

		log.info("Successfully Created Round Up Savings Goal with Id:{}...", savingsGoal.getSavingsGoalUid());
		return savingsGoal;
	}

	public static long calculateRoundUpSum(final FeedItems feedItems) {
		var sumDecimal = 0d;
		for (final FeedItem feedItem : feedItems.getFeedItems()) {
			final var decimalAmount  = feedItem.getAmount().getMinorUnits()/100d;
			final var roundUp = 1d - (decimalAmount % 1d);

			log.debug("Round Up {} Amount: {}", decimalAmount, roundUp);
			sumDecimal += roundUp;
		}

		final var roundUpSum = Math.round(sumDecimal * 100d); // round sub currency, so we don't get .xxxxx vals
		log.info("Total Round Up Amount: {}", roundUpSum);
		return roundUpSum;
	}

	private FeedItems getOutgoingsFeedItemsInPeriod(final String accountUid, final FeedItemsFilter feedItemsFilter) {
		final var feedItems = feedService.getOutgoingFeedItemsInPeriod(accountUid, feedItemsFilter);
		feedItems.setFeedItems(feedItems.getFeedItems()
				.stream()
				.filter(f -> f.getDirection().equals("OUT") && f.getAmount().getCurrency().equals(feedItemsFilter.getCurrency()))
				.collect(Collectors.toList()));
		return feedItems;
	}

	private Instant calculateToTime(final RoundUpSavingsGoalRequest request, final Instant fromTime) {
		return request.getMaxTransactionTimestamp() != null ? request.getMaxTransactionTimestamp() : fromTime.plus(Period.ofDays(starlingProperties.getDefaultRoundUpPeriod()));
	}

	private void validate(final RoundUpSavingsGoalRequest request) {
		if (null == request.getMinTransactionTimestamp()) {
			throw new IllegalArgumentException("Minimum Transaction Timestamp is Required");
		}
	}

	private long calculateTimeDifferenceInDays(final Instant minTransactionTimestamp, final Instant maxTransactionTimestamp) {
		return ChronoUnit.DAYS.between(minTransactionTimestamp, maxTransactionTimestamp);
	}
}
