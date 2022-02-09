package com.chaudhrii.sterlingtechtask.sterling;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.chaudhrii.sterlingtechtask.core.config.StarlingProperties;
import com.chaudhrii.sterlingtechtask.sterling.api.Account;
import com.chaudhrii.sterlingtechtask.sterling.api.Accounts;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class StarlingAccountsServiceTest {

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private StarlingProperties starlingProperties;

	@InjectMocks
	private StarlingAccountsService service;

	@Test
	void whenRetrieveAccounts_andAccountIsReturned() {
		// Given
		final var response = new ResponseEntity<>(new Accounts(List.of(new Account())), HttpStatus.OK);
		when(restTemplate.getForEntity(anyString(), eq(Accounts.class))).thenReturn(response);

		// When
		final var accounts = service.getAllAccounts();

		// Then
		assertNotNull(accounts);
		assertEquals(1, accounts.getAccounts().size());
	}
}