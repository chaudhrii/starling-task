package com.chaudhrii.sterlingtechtask.sterling.service.transaction;

import java.time.Instant;
import java.util.Collections;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.chaudhrii.sterlingtechtask.core.config.StarlingProperties;
import com.chaudhrii.sterlingtechtask.core.exception.StarlingException;
import com.chaudhrii.sterlingtechtask.sterling.api.FeedItems;
import com.chaudhrii.sterlingtechtask.sterling.api.SavingsGoalList;

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
	public FeedItems getOutgoingFeedItemsInPeriod(final String accountUid, final Instant fromTime, final Instant toTime) {
		var feedItems = new FeedItems(Collections.emptyList());

		try {
			log.debug("Getting All Feed Item Records for account: {} between {} and {} from Starling...", accountUid, fromTime, toTime);
			final var response =
					restTemplate.getForEntity(
							starlingProperties.getStarlingBaseUrl() + "/api/v2/feed/account/{accountUid}/settled-transactions-between?minTransactionTimestamp={fromTime}&maxTransactionTimestamp={toTime}",
							FeedItems.class,
							accountUid,
							fromTime.toString(),
							toTime.toString());
			final var responseBody = response.getBody();
			log.debug("Response: {}", response);

			if (null != responseBody) {
				feedItems = responseBody;
			}

		} catch (final Exception e) {
			log.error(FAILURE);
			throw new StarlingException(FAILURE, e);
		}

		log.debug("Retrieved {} Feed Items Records", feedItems.getFeedItems().size());
		return feedItems;
	}
}
