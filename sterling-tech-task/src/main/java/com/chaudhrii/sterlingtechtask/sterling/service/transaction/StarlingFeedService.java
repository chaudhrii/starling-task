package com.chaudhrii.sterlingtechtask.sterling.service.transaction;

import com.chaudhrii.sterlingtechtask.core.filter.FeedItemsFilter;
import com.chaudhrii.sterlingtechtask.sterling.api.FeedItems;

public interface StarlingFeedService {
	FeedItems getOutgoingFeedItemsInPeriod(final String accountUid, final FeedItemsFilter feedItemsFilter);
}
