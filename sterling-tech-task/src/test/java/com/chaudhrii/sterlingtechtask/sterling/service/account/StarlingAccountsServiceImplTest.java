package com.chaudhrii.sterlingtechtask.sterling.service.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

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
import com.chaudhrii.sterlingtechtask.core.exception.StarlingException;
import com.chaudhrii.sterlingtechtask.sterling.api.Account;
import com.chaudhrii.sterlingtechtask.sterling.api.Accounts;
import com.chaudhrii.sterlingtechtask.sterling.api.Balance;
import com.chaudhrii.sterlingtechtask.sterling.api.SignedCurrencyAndAmount;
import com.chaudhrii.sterlingtechtask.sterling.service.ErrorResponse;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class StarlingAccountsServiceImplTest {
	private static final String ACCOUNT_UID = "1db3f775-36e3-4e9f-bc9b-582cde60017f";

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private StarlingProperties starlingProperties;

	@InjectMocks
	private StarlingAccountsServiceImpl service;

	@Test
	void whenRetrieveAccounts_andAccountAreReturned() {
		// Given
		final var response = new ResponseEntity<>(new Accounts(List.of(new Account())), HttpStatus.OK);
		when(restTemplate.getForEntity(anyString(), eq(Accounts.class))).thenReturn(response);

		// When
		final var accounts = service.getAllAccounts();

		// Then
		assertNotNull(accounts);
		assertEquals(1, accounts.getAccounts().size());
	}

	@Test
	void whenRetrieveAccounts_andNoAccountFound() {
		// Given
		final var error = new ErrorResponse();
		final var response = new ResponseEntity<>((Object) error, HttpStatus.NOT_FOUND);
		when(restTemplate.getForEntity(anyString(), any())).thenReturn(response);

		// When/ Then
		final var ex = assertThrows(StarlingException.class, () -> service.getAllAccounts());
		assertEquals("Failed in Retrieving All Account Records from Starling", ex.getMessage());
	}

	@Test
	void whenRetrieveAccount_andAccountIsReturned() {
		// Given
		final var testBalance = Balance.of(SignedCurrencyAndAmount.of("GBP", 248016L));
		final var response = new ResponseEntity<>((Object) testBalance, HttpStatus.OK);
		when(restTemplate.getForEntity(anyString(), any(), eq(ACCOUNT_UID))).thenReturn(response);

		// When
		final var balance = service.getAccountBalance(ACCOUNT_UID);

		// Then
		assertNotNull(balance);
		assertEquals(testBalance, balance);
	}

	@Test
	void whenRetrieveAccount_andAccountNotFoundReturned() {
		// Given
		final var error = new ErrorResponse();
		final var response = new ResponseEntity<>((Object) error, HttpStatus.NOT_FOUND);
		when(restTemplate.getForEntity(anyString(), any(), eq(ACCOUNT_UID))).thenReturn(response);

		// When / Then
		final var ex = assertThrows(StarlingException.class, () -> service.getAccountBalance(ACCOUNT_UID));
		assertEquals(String.format("Failed in Retrieving Account Balance for account:%s from Starling", ACCOUNT_UID), ex.getMessage());
	}
}