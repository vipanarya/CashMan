package com.vipan.cashman.model;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class CompositeMoney {

	private ArrayList<Money> moneyInATM = new ArrayList<Money>();

	private int totalMoney;

	private WithdrawalError error;

	public int getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(int totalMoney) {
		this.totalMoney = totalMoney;
	}

	public ArrayList<Money> getMoneyInATM() {
		return moneyInATM;
	}

	public void setMoneyInATM(ArrayList<Money> moneyInATM) {
		this.moneyInATM = moneyInATM;
		this.totalMoney = this.moneyInATM.parallelStream().mapToInt(i -> i.getCount()*i.getDenomination()).sum();
	}

	public WithdrawalError getError() {
		return error;
	}

	public void setError(WithdrawalError error) {
		this.error = error;
	}

}
