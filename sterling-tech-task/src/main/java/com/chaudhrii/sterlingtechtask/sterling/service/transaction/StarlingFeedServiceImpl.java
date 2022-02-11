package com.chaudhrii.sterlingtechtask.sterling.service.transaction;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.chaudhrii.sterlingtechtask.core.config.StarlingProperties;
import com.chaudhrii.sterlingtechtask.core.exception.StarlingException;
import com.chaudhrii.sterlingtechtask.core.filter.FeedItemsFilter;
import com.chaudhrii.sterlingtechtask.sterling.api.FeedItem;
import com.chaudhrii.sterlingtechtask.sterling.api.FeedItems;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StarlingFeedServiceImpl implements StarlingFeedService {
	private static final String FAILURE = "Failed to get Feed Items by Account from Starling";

	private RestTemplate restTemplate;
	private StarlingProperties starlingProperties;

	public StarlingFeedServiceImpl(final RestTemplate restTemplate, final StarlingProperties starlingProperties) {
		this.restTemplate = restTemplate;
		this.starlingProperties = starlingProperties;
	}

	@Override
	public FeedItems getOutgoingFeedItemsInPeriod(final String accountUid, final FeedItemsFilter feedItemsFilter) {
		final var intent = String.format("Getting All Feed Item Records for account: %s between %s and %s from Starling...",
				accountUid, feedItemsFilter.getFromTime(), feedItemsFilter.getToTime());
		log.debug(intent);
		final var response =
				restTemplate.getForEntity(
						starlingProperties.getStarlingBaseUrl() + "/api/v2/feed/account/{accountUid}/settled-transactions-between?minTransactionTimestamp={fromTime}&maxTransactionTimestamp={toTime}",
						FeedItems.class,
						accountUid,
						feedItemsFilter.getFromTime().toString(),
						feedItemsFilter.getToTime().toString());

		log.debug("Response: {}", response);

		if (!response.getStatusCode().is2xxSuccessful()) {
			final var message = String.format("Failed %s", intent);
			throw new StarlingException(message);
		}

		return response.getBody();
	}
}
