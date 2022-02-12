package com.chaudhrii.sterlingtechtask.sterling.api.error;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import com.chaudhrii.sterlingtechtask.core.exception.StarlingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StarlingErrorHandler implements ResponseErrorHandler {

	@Override
	public boolean hasError(final ClientHttpResponse response) throws IOException {
		return new DefaultResponseErrorHandler().hasError(response);
	}

	@Override
	public void handleError(final ClientHttpResponse response) throws IOException {
		log.error("Starling Response Error: {}", response);
		if (response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
			throw new StarlingException(String.format("Starling Failed to Process Request. Status Code: %s", response.getStatusCode()));
		} else if (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
			throw new IllegalArgumentException(String.format("Starling Failed to Process Request. Status Code: %s, Message: %s", response.getStatusCode(), response.getBody()));
		}
	}
}
