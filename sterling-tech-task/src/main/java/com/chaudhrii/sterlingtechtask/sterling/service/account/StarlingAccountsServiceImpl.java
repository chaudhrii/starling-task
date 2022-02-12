package com.chaudhrii.sterlingtechtask.sterling.service.account;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.chaudhrii.sterlingtechtask.core.config.StarlingProperties;
import com.chaudhrii.sterlingtechtask.core.exception.StarlingException;
import com.chaudhrii.sterlingtechtask.sterling.api.Accounts;
import com.chaudhrii.sterlingtechtask.sterling.api.Balance;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StarlingAccountsServiceImpl implements StarlingAccountsService {
	private RestTemplate restTemplate;
	private StarlingProperties starlingProperties;

	public StarlingAccountsServiceImpl(final RestTemplate restTemplate, final StarlingProperties starlingProperties) {
		this.restTemplate = restTemplate;
		this.starlingProperties = starlingProperties;
	}

	@Override
	public Accounts getAllAccounts() {
		final var intent = "Retrieving All Account Records from Starling";
		log.debug(intent);
		final var response =
				restTemplate.getForEntity(
						starlingProperties.getStarlingBaseUrl() + "/api/v2/accounts",
						Accounts.class);
		log.debug("Response: {}", response);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new StarlingException("Failed in " + intent);
		}

		return response.getBody();
	}

	@Override
	public Balance getAccountBalance(final String accountUid) {
		final var intent = String.format("Retrieving Account Balance for account:%s from Starling", accountUid);
		log.debug(intent);
		final var response =
				restTemplate.getForEntity(
						starlingProperties.getStarlingBaseUrl() + "/api/v2/accounts/{accountUid}/balance",
						Balance.class,
						accountUid);
		log.debug("Response: {}", response);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new StarlingException("Failed in " + intent);
		}
		return response.getBody();
	}
}
