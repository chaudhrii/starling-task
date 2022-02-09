package com.chaudhrii.sterlingtechtask.sterling.service.account;

import com.chaudhrii.sterlingtechtask.sterling.api.Accounts;
import com.chaudhrii.sterlingtechtask.sterling.api.Balance;

public interface StarlingAccountsService {
	Accounts getAllAccounts();
	Balance getAccountBalance(final String accountUid);
}
