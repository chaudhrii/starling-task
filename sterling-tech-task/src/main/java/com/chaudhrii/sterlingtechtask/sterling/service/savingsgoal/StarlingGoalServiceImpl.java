package com.chaudhrii.sterlingtechtask.sterling.service.savingsgoal;

import java.util.Collections;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.chaudhrii.sterlingtechtask.core.config.StarlingProperties;
import com.chaudhrii.sterlingtechtask.core.exception.StarlingException;
import com.chaudhrii.sterlingtechtask.sterling.api.SavingsGoalList;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StarlingGoalServiceImpl {
	private static final String FAILURE = "Failed to get Saving Goals by Account Holder from Starling";
	private RestTemplate restTemplate;
	private StarlingProperties starlingProperties;

	public StarlingGoalServiceImpl(final RestTemplate restTemplate, final StarlingProperties starlingProperties) {
		this.restTemplate = restTemplate;
		this.starlingProperties = starlingProperties;
	}

	public SavingsGoalList getSavingsGoals(final String accountUid) {
		var goals = new SavingsGoalList(Collections.emptyList());

		try {
			log.debug("Getting All Account Records from Starling...");
			final var response =
					restTemplate.getForEntity(
							starlingProperties.getStarlingBaseUrl() + "/api/v2/account/{accountUid}/savings-goals",
							SavingsGoalList.class,
							accountUid);
			final var responseBody = response.getBody();
			log.debug("Response: {}", response);

			if (null != responseBody) {
				goals = responseBody;
			}

		} catch (final Exception e) {
			log.error(FAILURE);
			throw new StarlingException(FAILURE, e);
		}

		log.debug("Retrieved {} Saving Goal Records", goals.getSavingsGoals().size());
		return goals;
	}
}
