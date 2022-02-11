package com.chaudhrii.sterlingtechtask.sterling.service.savingsgoal;

import com.chaudhrii.sterlingtechtask.sterling.api.SavingsGoal;
import com.chaudhrii.sterlingtechtask.sterling.service.savingsgoal.request.SavingsGoalRequest;

public interface StarlingGoalService {
	SavingsGoal getSavingsGoal(final String accountUid, final String savingsGoalUid);
	SavingsGoal createSavingsGoal(final String accountUid, final SavingsGoalRequest request);
	void deleteSavingsGoal(final String accountUid, final String savingsGoalUid);
}
