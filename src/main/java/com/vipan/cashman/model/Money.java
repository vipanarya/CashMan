package com.vipan.cashman.model;

public class Money {

	private int denomination;
	private int count;

	public Money() {
		super();
	}

	public int getDenomination() {
		return denomination;
	}

	public void setDenomination(int denomination) {
		this.denomination = denomination;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "Money [denomination=" + denomination + ", count=" + count + "]";
	}

}
