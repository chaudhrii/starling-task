package com.chaudhrii.sterlingtechtask.sterling.service.savingsgoal;

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
	private RestTemplate restTemplate;
	private StarlingProperties starlingProperties;

	public StarlingGoalServiceImpl(final RestTemplate restTemplate, final StarlingProperties starlingProperties) {
		this.restTemplate = restTemplate;
		this.starlingProperties = starlingProperties;
	}

	public SavingsGoalList getSavingsGoals(final String accountUid) {
		final var intent = "Getting All Savings Goal Records from Starling";
		log.debug(intent);

		final var response =
				restTemplate.getForEntity(
						starlingProperties.getStarlingBaseUrl() + "/api/v2/account/{accountUid}/savings-goals",
						SavingsGoalList.class,
						accountUid);
		log.debug("Response: {}", response);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new StarlingException("Failed in " + intent);
		}

		return response.getBody();
	}

	public SavingsGoal getSavingsGoal(final String accountUid, final String savingsGoalUid) {
		final var intent = String.format("Getting Savings Goal from Starling for accountUid:%s and savingsGoalUid:%s", accountUid, savingsGoalUid);
		log.debug(intent);

		final var response =
				restTemplate.getForEntity(
						starlingProperties.getStarlingBaseUrl() + "/api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}",
						SavingsGoal.class,
						accountUid, savingsGoalUid);
		log.debug("Response: {}", response);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new StarlingException("Failed in " + intent);
		}

		return response.getBody();
	}

	@Override
	public SavingsGoal createSavingsGoal(final String accountUid, final SavingsGoalRequest request) {
		final var intent = String.format("Creating Savings Goal at Starling for accountUid:%s and Savings Goal Name:%s", accountUid, request.getName());
		log.debug(intent);
		final var requestEntity = RequestEntity.put(starlingProperties.getStarlingBaseUrl() + "/api/v2/account/{accountUid}/savings-goals", accountUid)
				.body(request);
		final var response = restTemplate.exchange(requestEntity, SavingsGoalResponse.class);
		log.debug("Response: {}", response);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new StarlingException("Failed in " + intent);
		}

		return SavingsGoal.of(response.getBody().getSavingsGoalUid());
	}

	/**
	 * Notes: Would consider using restTemplate.exchange() with an appropriate message.
	 *
	 * Since delete is not a requirement of the exercise specification, I am taking a liberty here.
	 */
	@Override
	public void deleteSavingsGoal(final String accountUid, final String savingsGoalUid) {
		final var intent = String.format("Deleting Savings Goal accountUid:%s, savingsGoalUid:%s at Starling...", accountUid, savingsGoalUid);
		log.debug(intent);
		restTemplate.delete(starlingProperties.getStarlingBaseUrl() + "/api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}", accountUid, savingsGoalUid);
	}
}
