package com.vipan.cashman.validator;

import org.springframework.stereotype.Component;

import com.vipan.cashman.model.CompositeMoney;
import com.vipan.cashman.model.WithdrawalError;

@Component
public class CashmanValidator {
	public WithdrawalError validateWithdrawl(int moneyRequested, CompositeMoney compositeMoney) {
		WithdrawalError withdrawalError = null;

		if (moneyRequested > compositeMoney.getTotalMoney()) {
			withdrawalError = new WithdrawalError();
			withdrawalError.setCode(1000);
			withdrawalError.setMessage("Do not have enough money in machine");
		} else if (moneyRequested < 0) {
			withdrawalError = new WithdrawalError();
			withdrawalError.setCode(2000);
			withdrawalError.setMessage("Negative amount withdrawl is not allowed");

		}
		return withdrawalError;
	}

}
