package com.vipan.cashman.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vipan.cashman.model.CompositeMoney;
import com.vipan.cashman.model.Money;
import com.vipan.cashman.model.WithdrawalError;
import com.vipan.cashman.util.Utility;
import com.vipan.cashman.validator.CashmanValidator;

@RestController
public class MoneyController {

	@Autowired
	private CompositeMoney compositeMoney;

	@Autowired
	private CashmanValidator cashmanValidator;

	@Autowired
	private Utility utility;

	@RequestMapping(value = "/initializeMoney", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CompositeMoney initializeMoney(@RequestBody Money[] moneyArray) {
		ArrayList<Money> moneyInATM = new ArrayList<Money>();
		for (Money money : moneyArray) {
			moneyInATM.add(money);
		}
		moneyInATM.sort((i, j) -> (j.getDenomination() - i.getDenomination()));
		compositeMoney.setMoneyInATM(moneyInATM);
		return compositeMoney;
	}

	@RequestMapping(value = "/money", produces = MediaType.APPLICATION_JSON_VALUE)
	public CompositeMoney money() {
		return this.compositeMoney;
	}

	@RequestMapping(value = "/addMoney", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CompositeMoney addMoney(@RequestBody Money[] moneyArray) {
		ArrayList<Money> additionalMoney = new ArrayList<Money>();
		for (Money money : moneyArray) {
			additionalMoney.add(money);
		}
		additionalMoney.sort((i, j) -> (j.getDenomination() - i.getDenomination()));
		ArrayList<Money> newMoneyList = utility.addMoreMoneyToExistingMoney(this.compositeMoney.getMoneyInATM(),
				additionalMoney);
		compositeMoney.setMoneyInATM(newMoneyList);
		return compositeMoney;
	}


	@RequestMapping(value = "/withdrawMoney", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CompositeMoney withdrawMoney(@RequestBody CompositeMoney money) {
		int moneyRequested = money.getTotalMoney();

		this.compositeMoney.getMoneyInATM().sort((i, j) -> (j.getDenomination() - i.getDenomination()));

		int totalMoneyInATM = this.compositeMoney.getTotalMoney();

		WithdrawalError withdrawalError = cashmanValidator.validateWithdrawl(moneyRequested, this.compositeMoney);

		if (null != withdrawalError) {
			money.setError(withdrawalError);
			money.setTotalMoney(0);
			return money;
		}
		ArrayList<Money> moneyToBeDispensed = new ArrayList<Money>();

		for (int i = 0; i < this.compositeMoney.getMoneyInATM().size(); i++) {
			Money moneyTemp = new Money();
			Money existing = this.compositeMoney.getMoneyInATM().get(i);
			int denominationTemp = existing.getDenomination();
			moneyTemp.setDenomination(denominationTemp);
			System.out.println("Money Requested ::::: " + moneyRequested);
			int countTemp = utility.suggestCount(denominationTemp, moneyRequested, i, existing.getCount());

			moneyTemp.setCount(countTemp);
			moneyToBeDispensed.add(moneyTemp);
			moneyRequested = moneyRequested
					- moneyToBeDispensed.get(i).getDenomination() * moneyToBeDispensed.get(i).getCount();

		}
		if (moneyRequested > 0) {
			withdrawalError = new WithdrawalError();
			withdrawalError.setCode(3000);
			withdrawalError
					.setMessage("Please try another withdraw, denominations are not supporting current withdrawal");
			money.setError(withdrawalError);
			money.setTotalMoney(0);
			return money;
		}

		money.setMoneyInATM(moneyToBeDispensed);
		
		this.compositeMoney.setMoneyInATM(utility.reduceMoneyInATM(this.compositeMoney.getMoneyInATM(), moneyToBeDispensed));
		return money;
	}


}
