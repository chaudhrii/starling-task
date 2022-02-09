package com.chaudhrii.sterlingtechtask.sterling.service.transaction;

import java.time.Instant;

import com.chaudhrii.sterlingtechtask.sterling.api.FeedItems;

public interface StarlingFeedService {

	FeedItems getOutgoingFeedItemsInPeriod(final String accountUid, final Instant fromTime, final Instant toTime);
}
