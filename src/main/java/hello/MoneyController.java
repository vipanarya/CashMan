package hello;

import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MoneyController {

	@Autowired
	private CompositeMoney compositeMoney;

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
		ArrayList<Money> newMoneyList = addMoreMoneyToExistingMoney(this.compositeMoney.getMoneyInATM(),
				additionalMoney);
		compositeMoney.setMoneyInATM(newMoneyList);
		return compositeMoney;
	}

	private ArrayList<Money> addMoreMoneyToExistingMoney(ArrayList<Money> moneyInATM,
			ArrayList<Money> additionalMoney) {
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
			if(!matched) {
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

	@RequestMapping(value = "/withdrawMoney", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CompositeMoney withdrawMoney(@RequestBody CompositeMoney money) {
		int moneyRequested = money.getTotalMoney();

		/*
		 * ArrayList<Money> moneyInATMStream = (ArrayList<Money>)
		 * this.compositeMoney.getMoneyInATM().parallelStream() .sorted((i, j)
		 * -> (j.getDenomination() -
		 * i.getDenomination())).collect(Collectors.toList());
		 * moneyInATMStream.forEach(System.out::println);
		 */
		this.compositeMoney.getMoneyInATM().sort((i, j) -> (j.getDenomination() - i.getDenomination()));
		this.compositeMoney.getMoneyInATM().forEach(System.out::println);

		int totalMoneyInATM = this.compositeMoney.getTotalMoney();

		ArrayList<Money> moneyToBeDispensed = new ArrayList<Money>();

		for (int i = 0; i < this.compositeMoney.getMoneyInATM().size(); i++) {
			Money moneyTemp = new Money();
			int denominationTemp = this.compositeMoney.getMoneyInATM().get(i).getDenomination();
			moneyTemp.setDenomination(denominationTemp);
			if (i > 0) {
				moneyRequested = moneyRequested
						- moneyToBeDispensed.get(i - 1).getDenomination() * moneyToBeDispensed.get(i - 1).getCount();
			}
			System.out.println("Money Requested ::::: " + moneyRequested);
			int countTemp = calculateCount(denominationTemp, moneyRequested, i);

			moneyTemp.setCount(countTemp);
			moneyToBeDispensed.add(moneyTemp);

		}
		money.setMoneyInATM(moneyToBeDispensed);
		reduceMoneyInATM(this.compositeMoney.getMoneyInATM(), moneyToBeDispensed);
		return money;
	}

	private void reduceMoneyInATM(ArrayList<Money> moneyInATM, ArrayList<Money> moneyToBeDispensed) {
		ArrayList<Money> newMoneyInATM = new ArrayList<Money>();
		for (int i = 0; i < this.compositeMoney.getMoneyInATM().size(); i++) {
			Money newMoney = new Money();
			newMoney.setCount(
					this.compositeMoney.getMoneyInATM().get(i).getCount() - moneyToBeDispensed.get(i).getCount());
			newMoney.setDenomination(moneyToBeDispensed.get(i).getDenomination());
			newMoneyInATM.add(newMoney);
		}
		this.compositeMoney.setMoneyInATM(newMoneyInATM);
	}

	private int calculateCount(int denomination, int moneyRequested, int index) {
		int count = moneyRequested / denomination;
		if (count < this.compositeMoney.getMoneyInATM().get(index).getCount()) {
			return count;
		} else if (count >= this.compositeMoney.getMoneyInATM().get(index).getCount()) {
			return this.compositeMoney.getMoneyInATM().get(index).getCount();
		}
		return 0;
	}
}
