package com.chaudhrii.sterlingtechtask.sterling.service.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.chaudhrii.sterlingtechtask.core.config.StarlingProperties;
import com.chaudhrii.sterlingtechtask.core.exception.StarlingException;
import com.chaudhrii.sterlingtechtask.core.filter.FeedItemsFilter;
import com.chaudhrii.sterlingtechtask.sterling.api.FeedItem;
import com.chaudhrii.sterlingtechtask.sterling.api.FeedItems;
import com.chaudhrii.sterlingtechtask.sterling.service.ErrorResponse;

@ExtendWith(MockitoExtension.class)
class StarlingFeedServiceImplTest {

	private static final String ACCOUNT_UID = "1db3f775-36e3-4e9f-bc9b-582cde60017f";
	private static final FeedItemsFilter FEED_ITEMS_FILTER =  FeedItemsFilter.of("GBP", Instant.now(), Instant.now());

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private StarlingProperties starlingProperties;

	@InjectMocks
	private StarlingFeedServiceImpl service;

	@Test
	void testGetAllFeedItems_andFeedItemsAreReturned() {
		// Given
		final var response = new ResponseEntity<>(new FeedItems(List.of(new FeedItem())), HttpStatus.OK);
		when(restTemplate.getForEntity(
				anyString(),
				eq(FeedItems.class),
				eq(ACCOUNT_UID),
				eq(FEED_ITEMS_FILTER.getFromTime().toString()),
				eq(FEED_ITEMS_FILTER.getToTime().toString()))
		).thenReturn(response);

		// When
		final var feedItems = service.getOutgoingFeedItemsInPeriod(ACCOUNT_UID, FEED_ITEMS_FILTER);

		// Then
		assertNotNull(feedItems);
		assertEquals(1, feedItems.getFeedItems().size());
	}

	@Test
	void testGetAllFeedItems_NoFeedItemsFound() {
		// Given
		final var error = new ErrorResponse();
		final var response = new ResponseEntity<>((Object) error, HttpStatus.NOT_FOUND);
		when(restTemplate.getForEntity(
				anyString(),
				any(),
				eq(ACCOUNT_UID),
				eq(FEED_ITEMS_FILTER.getFromTime().toString()),
				eq(FEED_ITEMS_FILTER.getToTime().toString()))
		).thenReturn(response);

		// When/Then
		assertThrows(StarlingException.class, () -> service.getOutgoingFeedItemsInPeriod(ACCOUNT_UID, FEED_ITEMS_FILTER));
	}
}