package com.chaudhrii.sterlingtechtask.service;

import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.chaudhrii.sterlingtechtask.api.RoundUpSavingsGoalRequest;
import com.chaudhrii.sterlingtechtask.core.exception.StarlingException;
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

	private static final int DEFAULT_TIMESPAN_DAYS = 7;
	//private static final DecimalFormat F = new DecimalFormat("##.00");

	private StarlingAccountsService accountsService;
	private StarlingFeedService feedService;
	private StarlingGoalService goalService;

	public RoundUpSavingsGoalServiceImpl(final StarlingAccountsService accountsService, final StarlingFeedService feedService, final StarlingGoalService goalService) {
		this.accountsService = accountsService;
		this.feedService = feedService;
		this.goalService = goalService;
	}

	public SavingsGoal createRoundUpSavingsGoal(final String accountUid, final RoundUpSavingsGoalRequest request) {
		final var balance = accountsService.getAccountBalance(accountUid);
		if (null == balance) {
			throw new IllegalArgumentException("Account does not exist!");
		}

		validate(request);
		final var currency = balance.getAmount().getCurrency();

		final var fromTime = request.getMinTransactionTimestamp();
		final var toTime = calculateToTime(request, fromTime);

		log.debug("Days in between: {}", calculateTimeDifferenceInDays(fromTime, toTime));

		// TODO: check goal doesn't already exist

		final var feedItems= getOutgoingsInPeriod(accountUid, currency, fromTime, toTime);
		log.debug("Considering {} Outgoing Feed Items", feedItems.getFeedItems().size());

		// Round up sum
		final var roundUpSum = calculateRoundUpSum(feedItems);
		log.debug("Round up sum {}", roundUpSum);

		// TODO : If Sum is 0 - no point creating a goal?

		// create savings goal
		final var savingsGoalRequest = SavingsGoalRequest.of(request.getSavingsGoalName(), currency, CurrencyAndAmount.of(currency, roundUpSum));
		final var savingsGoal = goalService.createSavingsGoal(accountUid, savingsGoalRequest);

		if (null != savingsGoal && savingsGoal.getSavingsGoalUid() != null) {
			log.info("Successfully completed Round Up Savings Goal Task Creation...");
		} else {
			throw new StarlingException("Failed Round Up Savings Goal Task Creation...");
		}

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

	private FeedItems getOutgoingsInPeriod(final String accountUid, final String currency, final Instant fromTime, final Instant toTime) {
		final var feedItems = feedService.getOutgoingFeedItemsInPeriod(accountUid, fromTime, toTime);
		feedItems.setFeedItems(feedItems.getFeedItems()
				.stream()
				.filter(f -> f.getDirection().equals("OUT") && f.getAmount().getCurrency().equals(currency))
				.collect(Collectors.toList()));
		return feedItems;
	}

	private Instant calculateToTime(final RoundUpSavingsGoalRequest request, final Instant fromTime) {
		return request.getMaxTransactionTimestamp() != null ? request.getMaxTransactionTimestamp() : fromTime.plus(Period.ofDays(DEFAULT_TIMESPAN_DAYS));
	}

	private void validate(final RoundUpSavingsGoalRequest request) {
		if (null == request.getMinTransactionTimestamp()) {
			throw new IllegalArgumentException("From Time is Required");
		}
	}

	private long calculateTimeDifferenceInDays(final Instant minTransactionTimestamp, final Instant maxTransactionTimestamp) {
		return ChronoUnit.DAYS.between(minTransactionTimestamp, maxTransactionTimestamp);
	}
}
