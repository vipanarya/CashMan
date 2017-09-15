package hello;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MoneyController {

	private int count20;

	private int count50;

	@RequestMapping(value = "/initializeMoney", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Money initializeMoney(@RequestBody Money money) {
		System.out.println(money);
		count20 = money.getCount20();
		count50 = money.getCount50();
		money = new Money(count20, count50);
		return money;
	}

	@RequestMapping(value = "/money", produces = MediaType.APPLICATION_JSON_VALUE)
	public Money money() {
		Money money = new Money(count20, count50);
		return money;
	}

	@RequestMapping(value = "/addMoney", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Money addMoney(@RequestBody Money money) {
		count20 = count20 + money.getCount20();
		count50 = count50 + money.getCount50();
		money = new Money(count20, count50);
		return money;
	}

	@RequestMapping(value = "/reduceMoney", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Money reduceMoney(@RequestBody Money money) {
		count20 = count20 - money.getCount20();
		count50 = count50 - money.getCount50();
		if (count20 < 0) {
			count20 = 0;
		}
		if (count50 < 0) {
			count50 = 0;
		}
		money = new Money(count20, count50);
		return money;
	}

	@RequestMapping(value = "/withdrawMoney", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Money withdrawMoney(@RequestBody Money money) {
		int moneyRequested = money.getTotalMoney();

		WithdrawalError error = validateWithdrawl(count20, count50, moneyRequested);

		if (null != error) {
			money.setError(error);
			money.setTotalMoney(0);
			return money;
		}

		money = dispenseMoney(count20, count50, moneyRequested);
		return money;
	}

	public WithdrawalError validateWithdrawl(int count20, int count50, int moneyRequested) {

		WithdrawalError withdrawalError = null;

		int totalRemainingMoney = 20 * count20 + 50 * count50;

		if (moneyRequested > totalRemainingMoney) {
			withdrawalError = new WithdrawalError();
			withdrawalError.setCode(1000);
			withdrawalError.setMessage("Do not have enough money in machine");
		} else if (moneyRequested % 50 != 0 && moneyRequested % 20 != 0 && moneyRequested % 70 != 0) {
			withdrawalError = new WithdrawalError();
			withdrawalError.setCode(2000);
			withdrawalError.setMessage("Cannot Dispense due to Denominations not available");

		} else if (moneyRequested < 0) {
			withdrawalError = new WithdrawalError();
			withdrawalError.setCode(3000);
			withdrawalError.setMessage("Negative amount withdrawl is not allowed");

		}
		return withdrawalError;
	}

	public Money dispenseMoney(int count20, int count50, int moneyRequested) {
		WithdrawalError withdrawalError = null;
		int dispensedCountFor50 = calculateCount(50, moneyRequested);
		int remainderMoney = moneyRequested - dispensedCountFor50 * 50;
		int dispensedCountFor20 = calculateCount(20, remainderMoney);
		remainderMoney = remainderMoney - dispensedCountFor20 * 20;

		if (remainderMoney > 0) {
			withdrawalError = new WithdrawalError();
			withdrawalError.setCode(4000);
			withdrawalError.setMessage("Please try withdrawl with different value");
			dispensedCountFor50 = 0;
			dispensedCountFor20 = 0;
		} else {
			this.count50 = this.count50 - dispensedCountFor50;
			this.count20 = this.count20 - dispensedCountFor20;
		}

		Money money = new Money(dispensedCountFor20, dispensedCountFor50);
		money.setError(withdrawalError);
		return money;

	}

	private int calculateCount(int denomination, int moneyRequested) {
		if (denomination == 50) {
			int count = moneyRequested / denomination;
			if (count < this.count50) {
				return count;
			} else if (count >= this.count50) {
				return this.count50;
			}
		}
		if (denomination == 20) {
			int count = moneyRequested / denomination;
			if (count < this.count20) {
				return count;
			} else if (count >= this.count20) {
				return this.count20;
			}
		}
		return 0;
	}
}
