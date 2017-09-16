package com.vipan.cashman.util;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.vipan.cashman.model.Money;

@Component
public class Utility {

	public ArrayList<Money> addMoreMoneyToExistingMoney(ArrayList<Money> moneyInATM, ArrayList<Money> additionalMoney) {
		System.out.println(moneyInATM);
		System.out.println(additionalMoney);
		ArrayList<Money> newDenominationsAddedList = new ArrayList<Money>();
		for (int i = 0; i < additionalMoney.size(); i++) {
			int denomination = additionalMoney.get(i).getDenomination();
			int count = additionalMoney.get(i).getCount();
			boolean matched = false;
			for (int j = 0; j < moneyInATM.size(); j++) {
				if (denomination == moneyInATM.get(j).getDenomination()) {
					matched = true;
					System.out.println("Denomination Matched");
					int existingCount = moneyInATM.get(j).getCount();
					moneyInATM.get(j).setCount(existingCount + count);
					System.out.println("Incremented Count");
					break;
				}
			}
			if (!matched) {
				Money money = new Money();
				money.setCount(count);
				money.setDenomination(denomination);
				newDenominationsAddedList.add(money);
			}
		}
		// combine two lists
		ArrayList<Money> combinedList = new ArrayList<Money>();
		combinedList.addAll(moneyInATM);
		combinedList.addAll(newDenominationsAddedList);
		combinedList.sort((i, j) -> (j.getDenomination() - i.getDenomination()));
		return combinedList;
	}

	public ArrayList<Money> reduceMoneyInATM(ArrayList<Money> moneyInATM, ArrayList<Money> moneyToBeDispensed) {
		ArrayList<Money> newMoneyInATM = new ArrayList<Money>();
		for (int i = 0; i < moneyInATM.size(); i++) {
			Money newMoney = new Money();
			newMoney.setCount(moneyInATM.get(i).getCount() - moneyToBeDispensed.get(i).getCount());
			newMoney.setDenomination(moneyToBeDispensed.get(i).getDenomination());
			newMoneyInATM.add(newMoney);
		}
		return newMoneyInATM;
	}

	public int suggestCount(int denomination, int moneyRequested, int index, int existingCount) {
		int count = moneyRequested / denomination;
		if (count < existingCount) {
			return count;
		} else if (count >= existingCount) {
			return existingCount;
		}
		return 0;
	}

}
