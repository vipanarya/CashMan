package hello;

public class Money {

	private int count20;
	private int count50;
	private int totalMoney;

	private WithdrawalError error;

	public Money() {
		super();
	}

	public Money(int count20, int count50) {
		super();
		this.count20 = count20;
		this.count50 = count50;
		this.totalMoney = 20 * count20 + 50 * count50;
	}

	public int getCount20() {
		return count20;
	}

	public void setCount20(int count20) {
		this.count20 = count20;
	}

	public int getCount50() {
		return count50;
	}

	public void setCount50(int count50) {
		this.count50 = count50;
	}

	public int getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(int totalMoney) {
		this.totalMoney = totalMoney;
	}

	public WithdrawalError getError() {
		return error;
	}

	public void setError(WithdrawalError error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "Money [count20=" + count20 + ", count50=" + count50 + ", totalMoney=" + totalMoney + "]";
	}

}
