package com.abc;

import java.text.ParseException;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BankTest {
    private static final double DOUBLE_DELTA = 1e-15;

    @Test
    public void customerSummary() {
        Bank bank = new Bank();
        Customer john = new Customer("John");
        john.openAccount(new Account(Account.CHECKING));
        bank.addCustomer(john);

        assertEquals("Customer Summary\n - John (1 account)", bank.customerSummary());
    }

    @Test
    public void checkingAccount() throws ParseException {
    	//I made the change, as now interest are accrued by day
    	//I changed the account opening date (first transaction date) to 2014-10-22
        Bank bank = new Bank();
        Account checkingAccount = new Account(Account.CHECKING);
        Customer bill = new Customer("Bill").openAccount(checkingAccount);
        bank.addCustomer(bill);

        checkingAccount.deposit(100.0);
        checkingAccount.transactions.get(0).lastYear();

        assertEquals(0.1, bank.totalInterestPaid(), 1e-2);
    }

    @Test
    public void savings_account(){
        Bank bank = new Bank();
        Account checkingAccount = new Account(Account.SAVINGS);
        bank.addCustomer(new Customer("Bill").openAccount(checkingAccount));

        checkingAccount.deposit(1500.0);
        checkingAccount.transactions.get(0).lastYear();
        
        assertEquals(2.0, bank.totalInterestPaid(), 1e-2);//(1500-1000)*0.002+1000*0.001
    }

    @Test
    public void maxi_savings_account() {
        Bank bank = new Bank();
        Account checkingAccount = new Account(Account.MAXI_SAVINGS);
        bank.addCustomer(new Customer("Bill").openAccount(checkingAccount));
        checkingAccount.deposit(3000.0);
        checkingAccount.transactions.get(0).lastYear();
        assertEquals(150.0, bank.totalInterestPaid(), 1e-2);//3000*0.05 = 150
    }
    
    //More tests on interests
    @Test
    public void maxi_savings_account2(){
    	//This is to test the interest rate change of withdrawal within 10 days
    	Bank bank = new Bank();
        Account checkingAccount = new Account(Account.MAXI_SAVINGS);
        bank.addCustomer(new Customer("Bill").openAccount(checkingAccount));
        checkingAccount.deposit(3000.0);
        checkingAccount.transactions.get(0).lastYear();//set the deposit last year
        checkingAccount.withdraw(1000.0);
        checkingAccount.transactions.get(1).tenDaysAgo();//set the withdrawal 10 days ago.
        assertEquals(145.95, bank.totalInterestPaid(), 1e-2);
    }
    
    @Test
    public void savings_account2() {
    	//This is to test the interest rate change of withdrawal and balance less than 1000
        Bank bank = new Bank();
        Account checkingAccount = new Account(Account.SAVINGS);
        bank.addCustomer(new Customer("Bill").openAccount(checkingAccount));
        checkingAccount.deposit(3000.0);
        checkingAccount.transactions.get(0).lastYear();//set the deposit last year
        checkingAccount.withdraw(2500.0);
        checkingAccount.transactions.get(1).tenDaysAgo();//set the withdrawal 10 days ago.
        assertEquals(4.90, bank.totalInterestPaid(), 1e-2);//3000*0.05 = 150
    }
    
    @Test
    public void maxi_savings_account3(){
    	//This is to test the interest rate change of withdrawal within 10 days
    	Bank bank = new Bank();
        Account checkingAccount = new Account(Account.MAXI_SAVINGS);
        bank.addCustomer(new Customer("Bill").openAccount(checkingAccount));
        checkingAccount.deposit(3000.0);
        checkingAccount.transactions.get(0).lastYear();//set the deposit last year
        checkingAccount.withdraw(1000.0);
        checkingAccount.transactions.get(1).hundredDaysAgo();//set the withdrawal 100 days ago.
        checkingAccount.withdraw(1000.0);
        checkingAccount.transactions.get(2).tenDaysAgo();//set the withdrawal 10 days ago.
        assertEquals(130.90, bank.totalInterestPaid(), 1e-2);
    }
    
}
