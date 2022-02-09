package com.chaudhrii.sterlingtechtask.sterling.service.account;

import java.util.Collections;

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
	private static final String FAILURE = "Failed to get Accounts by Account Holder from Starling";

	private RestTemplate restTemplate;
	private StarlingProperties starlingProperties;

	public StarlingAccountsServiceImpl(final RestTemplate restTemplate, final StarlingProperties starlingProperties) {
		this.restTemplate = restTemplate;
		this.starlingProperties = starlingProperties;
	}

	@Override
	public Accounts getAllAccounts() {
		var accounts = new Accounts(Collections.emptyList());

		try {
			log.debug("Getting All Account Records from Starling...");
			final var response =
					restTemplate.getForEntity(
							starlingProperties.getStarlingBaseUrl() + "/api/v2/accounts",
							Accounts.class);
			final var responseBody = response.getBody();
			log.debug("Response: {}", response);

			if (null != responseBody) {
				accounts = responseBody;
			}

		} catch (final Exception e) {
			log.error(FAILURE);
			throw new StarlingException(FAILURE, e);
		}

		log.debug("Retrieved {} Account Records", accounts.getAccounts().size());
		return accounts;
	}

	public Balance getAccountBalance(final String accountUid) {
		Balance balance = null;
		try {
			log.debug("Getting Account Balance for account: {} from Starling...", accountUid);
			final var response =
					restTemplate.getForEntity(
							starlingProperties.getStarlingBaseUrl() + "/api/v2/accounts/{accountUid}/balance",
							Balance.class,
							accountUid);
			final var responseBody = response.getBody();
			log.debug("Response: {}", response);

			if (null != responseBody) {
				balance = responseBody;
			}

		} catch (final Exception e) {
			log.error(FAILURE);
			throw new StarlingException(FAILURE, e);
		}
		return balance;
	}
}
