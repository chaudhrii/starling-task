package com.chaudhrii.sterlingtechtask.sterling;

import java.util.Collections;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.chaudhrii.sterlingtechtask.core.config.StarlingProperties;
import com.chaudhrii.sterlingtechtask.core.exception.StarlingException;
import com.chaudhrii.sterlingtechtask.sterling.api.Accounts;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StarlingAccountsService {
	private static final String FAILURE = "Failed to get Accounts by Account Holder from Starling";

	private RestTemplate restTemplate;
	private StarlingProperties starlingProperties;

	public StarlingAccountsService(final RestTemplate restTemplate, final StarlingProperties starlingProperties) {
		this.restTemplate = restTemplate;
		this.starlingProperties = starlingProperties;
	}

	public Accounts getAllAccounts() {
		var accounts = new Accounts(Collections.emptyList());

		try {
			log.debug("Calling Starling Bank External Client API...");

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

		return accounts;
	}
}
