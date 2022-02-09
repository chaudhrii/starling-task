package com.chaudhrii.sterlingtechtask.sterling.service.transaction;

import java.time.Instant;

import org.springframework.web.client.RestTemplate;

import com.chaudhrii.sterlingtechtask.core.config.StarlingProperties;
import com.chaudhrii.sterlingtechtask.sterling.api.FeedItems;

public class StarlingFeedServiceImpl {

	private RestTemplate restTemplate;
	private StarlingProperties starlingProperties;

	public StarlingFeedServiceImpl(final RestTemplate restTemplate, final StarlingProperties starlingProperties) {
		this.restTemplate = restTemplate;
		this.starlingProperties = starlingProperties;
	}

	public FeedItems getFeedItemsBetweenDates(final String accountUid, final Instant fromTime, final Instant toTime) {
		return null;
	}
}
