package com.chaudhrii.sterlingtechtask.service;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.chaudhrii.sterlingtechtask.api.RoundUpSavingsGoalRequest;
import com.chaudhrii.sterlingtechtask.sterling.api.FeedItem;
import com.chaudhrii.sterlingtechtask.sterling.api.FeedItems;
import com.chaudhrii.sterlingtechtask.sterling.service.account.StarlingAccountsService;
import com.chaudhrii.sterlingtechtask.sterling.service.transaction.StarlingFeedService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RoundUpSavingsGoalServiceImpl {

	private static final int DEFAULT_TIMESPAN_DAYS = 7;
	private static final DecimalFormat F = new DecimalFormat("##.00");

	private StarlingAccountsService accountsService;
	private StarlingFeedService feedService;

	public RoundUpSavingsGoalServiceImpl(final StarlingAccountsService accountsService, final StarlingFeedService feedService) {
		this.accountsService = accountsService;
		this.feedService = feedService;
	}

	public void createRoundUpSavingsGoal(final String accountUid, final RoundUpSavingsGoalRequest request) {
		validate(accountUid, request);

		final var fromTime = request.getMinTransactionTimestamp();
		final var toTime = calculateToTime(request, fromTime);

		log.debug("Days in between: {}", calculateTimeDifferenceInDays(fromTime, toTime));

		// TODO: check goal doesn't already exist

		final var feedItems= getOutgoingsInPeriod(accountUid, fromTime, toTime);
		log.debug("Considering {} Outgoing Feed Items", feedItems.getFeedItems().size());

		// Round up sum
		final var roundUpSum = calculateRoundUpSum(feedItems);
		log.debug("Round up sum {}", roundUpSum);

		// Round to nearest 2dec pl before creating goal
	}

	public static long calculateRoundUpSum(final FeedItems feedItems) {
		var sumDecimal = 0d;
		for (final FeedItem feedItem : feedItems.getFeedItems()) {
			final var decimalAmount  = feedItem.getAmount().getMinorUnits()/100d;

			final var subAmount = decimalAmount % 1d;
			if (decimalAmount != 1d) {
				final var roundUp = 1d - subAmount;
				log.debug("Round Up {} Amount: {}", decimalAmount, roundUp);
				sumDecimal += roundUp;
			}
		}

		var suml = Math.round(sumDecimal * 100d); // round sub currency
		log.info("Total Round Up Amount: {}", suml);

		return suml;
	}

	private FeedItems getOutgoingsInPeriod(final String accountUid, final Instant fromTime, final Instant toTime) {
		final var feedItems = feedService.getOutgoingFeedItemsInPeriod(accountUid, fromTime, toTime);
		feedItems.setFeedItems(feedItems.getFeedItems().stream().filter(f -> f.getDirection().equals("OUT")).collect(Collectors.toList()));
		return feedItems;
	}

	private Instant calculateToTime(final RoundUpSavingsGoalRequest request, final Instant fromTime) {
		return request.getMaxTransactionTimestamp() != null ? request.getMaxTransactionTimestamp() : fromTime.plus(Period.ofDays(DEFAULT_TIMESPAN_DAYS));
	}

	private void validate(final String accountUid, final RoundUpSavingsGoalRequest request) {
		final var balance = accountsService.getAccountBalance(accountUid);
		if (null == balance) {
			throw new IllegalArgumentException("Account does not exist!");
		}

		if (null == request.getMinTransactionTimestamp()) {
			throw new IllegalArgumentException("From Time is Required");
		}
	}

	private long calculateTimeDifferenceInDays(final Instant minTransactionTimestamp, final Instant maxTransactionTimestamp) {
		return ChronoUnit.DAYS.between(minTransactionTimestamp, maxTransactionTimestamp);
	}
}
