package com.chaudhrii.sterlingtechtask.sterling.service.savingsgoal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.chaudhrii.sterlingtechtask.core.config.StarlingProperties;
import com.chaudhrii.sterlingtechtask.core.exception.StarlingException;
import com.chaudhrii.sterlingtechtask.sterling.api.SavingsGoal;
import com.chaudhrii.sterlingtechtask.sterling.api.SavingsGoalList;
import com.chaudhrii.sterlingtechtask.sterling.service.savingsgoal.request.SavingsGoalRequest;
import com.chaudhrii.sterlingtechtask.sterling.service.savingsgoal.response.SavingsGoalResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StarlingGoalServiceImpl implements StarlingGoalService {
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
			log.debug("Getting All Savings Goal Records from Starling...");
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

	public SavingsGoal getSavingsGoal(final String accountUid, final String savingsGoalUid) {
		SavingsGoal goal = null;

		try {
			log.debug("Getting Savings Goal from Starling...");
			final var response =
					restTemplate.getForEntity(
							starlingProperties.getStarlingBaseUrl() + "/api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}",
							SavingsGoal.class,
							accountUid, savingsGoalUid);
			final var responseBody = response.getBody();
			log.debug("Response: {}", response);

			if (null != responseBody) {
				goal = responseBody;
			}

		} catch (final Exception e) {
			log.error(FAILURE);
			throw new StarlingException(FAILURE, e);
		}

		log.debug("Retrieved {} Saving Goal Record", goal);
		return goal;
	}

	@Override
	public SavingsGoal createSavingsGoal(final String accountUid, final SavingsGoalRequest request) {
		SavingsGoal goal = null;
		try {
			log.debug("Creating Savings Goal at Starling...");
			final var requestEntity = RequestEntity.put(starlingProperties.getStarlingBaseUrl() + "/api/v2/account/{accountUid}/savings-goals", accountUid)
					.body(request);
			final var response = restTemplate.exchange(requestEntity, SavingsGoalResponse.class);
			final var responseBody = response.getBody();
			log.debug("Response: {}", response);

			if (null != responseBody) {
				goal = SavingsGoal.of(responseBody.getSavingsGoalUid());
			}
		} catch (final Exception e) {
			log.error(FAILURE);
			throw new StarlingException(FAILURE, e);
		}

		log.debug("Created {} Saving Goal Record", goal);
		return goal;
	}
}
