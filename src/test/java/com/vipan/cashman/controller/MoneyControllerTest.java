package com.vipan.cashman.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vipan.cashman.Application;
import com.vipan.cashman.model.CompositeMoney;
import com.vipan.cashman.model.Money;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class MoneyControllerTest {
	
//	@Mock
	//private CompositeMoney compositeMoney;
	
//	@InjectMocks
	@Autowired
	private MoneyController moneyController;
	
	
	@Before
	public void setUp() throws Exception {
		System.out.println(1);
	}

	@Test
	public final void testMoney() {
		init();
		CompositeMoney cm = moneyController.money();
		Assert.assertEquals(20, cm.getTotalMoney());
	}

	@Test
	public final void testInitializeMoney() {
		Money money1 = new Money();
		money1.setCount(2);
		money1.setDenomination(10);
		Money[] moneyArray = {money1};
		CompositeMoney cm = moneyController.initializeMoney(moneyArray);
		Assert.assertEquals(20, cm.getTotalMoney());
		CompositeMoney cm1 = moneyController.money();
		Assert.assertEquals(20, cm1.getTotalMoney());
	}


	@Test
	public final void testAddMoney() {
		init();
		Money money2 = new Money();
		money2.setCount(2);
		money2.setDenomination(10);
		Money[] moneyArray2 = {money2};
		CompositeMoney cm2 = moneyController.addMoney(moneyArray2);
		Assert.assertEquals(40, cm2.getTotalMoney());
		
		CompositeMoney cm1 = moneyController.money();
		Assert.assertEquals(40, cm1.getTotalMoney());
	}

	@Test
	public final void testAddMoneyOfDifferentDenomination() {
		init();
		Money money2 = new Money();
		money2.setCount(2);
		money2.setDenomination(100);
		Money[] moneyArray2 = {money2};
		CompositeMoney cm2 = moneyController.addMoney(moneyArray2);
		Assert.assertEquals(220, cm2.getTotalMoney());
		
		CompositeMoney cm1 = moneyController.money();
		Assert.assertEquals(220, cm1.getTotalMoney());
	}

	@Test
	public final void testWithdrawMoney() {
		init();
		CompositeMoney cm3 = new CompositeMoney();
		cm3.setTotalMoney(10);
		
		CompositeMoney cm4 = moneyController.withdrawMoney(cm3);
		Assert.assertEquals(10, cm4.getTotalMoney());
		
		CompositeMoney cm1 = moneyController.money();
		Assert.assertEquals(10, cm1.getTotalMoney());
	}

	@Test
	public final void testWithdrawMoneyWithNegativeMoney() {
		init();
		CompositeMoney cm3 = new CompositeMoney();
		cm3.setTotalMoney(-10);
		
		CompositeMoney cm4 = moneyController.withdrawMoney(cm3);
		Assert.assertEquals(2000, cm4.getError().getCode());
		
		CompositeMoney cm1 = moneyController.money();
		Assert.assertEquals(20, cm1.getTotalMoney());
	}

	@Test
	public final void testWithdrawMoneyWithExcessMoney() {
		init();
		CompositeMoney cm3 = new CompositeMoney();
		cm3.setTotalMoney(10000);
		
		CompositeMoney cm4 = moneyController.withdrawMoney(cm3);
		Assert.assertEquals(1000, cm4.getError().getCode());
		
		CompositeMoney cm1 = moneyController.money();
		Assert.assertEquals(20, cm1.getTotalMoney());
	}

	@Test
	public final void testWithdrawMoneyForDenominationFailure() {
		init();
		CompositeMoney cm3 = new CompositeMoney();
		cm3.setTotalMoney(11);
		
		CompositeMoney cm4 = moneyController.withdrawMoney(cm3);
		Assert.assertEquals(3000, cm4.getError().getCode());
		
		CompositeMoney cm1 = moneyController.money();
		Assert.assertEquals(20, cm1.getTotalMoney());
	}

	private void init() {
		Money money1 = new Money();
		money1.setCount(2);
		money1.setDenomination(10);
		Money[] moneyArray = {money1};
		CompositeMoney cm = moneyController.initializeMoney(moneyArray);
		Assert.assertEquals(20, cm.getTotalMoney());
	}
}
