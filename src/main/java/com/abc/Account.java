package com.abc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Account {

    public static final int CHECKING = 0;
    public static final int SAVINGS = 1;
    public static final int MAXI_SAVINGS = 2;

    private final int accountType;
    public List<Transaction> transactions;
    
    //I added another private variable as balance 
    //(it feels really weird that the account does not have "check balance");
    private double balance;

    public Account(int accountType) {
        this.accountType = accountType;
        this.transactions = new ArrayList<Transaction>();
        this.balance = 0;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be greater than zero");
        } else {
        	this.balance+=amount;
            transactions.add(new Transaction(amount));
        }
    }

public void withdraw(double amount) {
    if (amount <= 0) {
        throw new IllegalArgumentException("amount must be greater than zero");
    } else if(amount > this.balance){
    	throw new IllegalArgumentException("not enough balance");
    } else {
    	this.balance-=amount;
        transactions.add(new Transaction(-amount));
    }
}

    public double interestEarned() {
    	//If interest is accrued daily, then we need to design a method to get every transaction,
    	//for every period between two consecutive transactions, Δint = previous_balance * interval * days/365* interest rate;
        switch(accountType){
            case SAVINGS:
                return calc_saving();
            case MAXI_SAVINGS:
                return calc_maxi_saving();
            default:
                return calc_checking();
        }
    }
    
    //This is used to calculate interest rate for checking
    //Basic principle: find every transaction, calculate the intervals between two consecutive transaction,
    //Δint = previous_balance * interval/365* annual_interest_rate;
    //Accumulate interest
    private double calc_checking(){
    	double interest = 0.00;
    	double balance = 0.00;
    	Date prev = this.transactions.get(0).getDate();
    	Date cur;
    	long interval = 0;
    	double accumulate = 0.00;
    	for(int i = 0; i < transactions.size(); i++){
    		Transaction t = this.transactions.get(i);
    		if(i > 0){
    			cur = t.getDate();
    			interval = getDateDiff(prev, cur, TimeUnit.DAYS);
    			accumulate = balance * interval * 0.001 / 365;
    			interest += accumulate;
    			prev = cur;
    		}
    		balance += t.getAmount(); //We need to process balance before moving towards next step.
    		if(i == transactions.size() - 1){
    			//If there is only one transaction or we've iterated through the final transaction
    			//We need to calculate what interest of that balance, to today
    			cur = new Date();
    			interval = getDateDiff(prev, cur, TimeUnit.DAYS);
    			accumulate = balance * interval * 0.001 / 365;
    			interest += accumulate;
    		}
    	}
    	return interest;
    }
    
    //This is used to calculate interest rate for saving
    //Basic principle: find every transaction, calculate the intervals between two consecutive transaction,
    //Δint = previous_balance * interval/365* annual_interest_rate;
    //Unlike checking, the annual interest rate of saving varies with balance;
    private double calc_saving(){
    	double annual_rate = 0.001;//
    	double interest = 0.00;
    	double balance = 0.00;
    	Date prev = this.transactions.get(0).getDate();
    	Date cur;
    	long interval = 0;
    	double accumulate = 0.00;
    	for(int i = 0; i < transactions.size(); i++){
    		Transaction t = this.transactions.get(i);
    		if(i > 0){
    			cur = t.getDate();
    			interval = getDateDiff(prev, cur, TimeUnit.DAYS);
    			if(balance < 1000){
    				accumulate = balance * interval * annual_rate / 365;
    			} else {
    				accumulate = 1 + (balance - 1000) * interval * annual_rate * 2 / 365;
    			}
    			interest += accumulate;
    			prev = cur;
    		}
    		balance += t.getAmount(); //We need to process balance before moving towards next step.
    		if(i == transactions.size() - 1){
    			//If there is only one transaction or we've iterated through the final transaction
    			//We need to calculate what interest of that balance, to today
    			cur = new Date();
    			interval = getDateDiff(prev, cur, TimeUnit.DAYS);
    			if(balance < 1000){
    				accumulate = balance * interval * annual_rate / 365;
    			} else {
    				accumulate = 1 + (balance - 1000) * interval * annual_rate * 2 / 365;
    			}
    			interest += accumulate;
    		}
    	}
    	return interest;
    }
    
  //This is used to calculate interest rate for maxi-saving
    //Basic principle: find every transaction, calculate the intervals between two consecutive transaction,
    //Δint = previous_balance * interval/365* annual_interest_rate;
    //Unlike checking and saving, the annual interest rate of maxi saving varies with condition whether there is withdrawal within 10days;
    private double calc_maxi_saving(){
    	double annual_rate = 0.05;//
    	double interest = 0.00;
    	double balance = 0.00;
    	Date prev = this.transactions.get(0).getDate();
    	Date cur;
    	long interval = 0;
    	double accumulate = 0.00;
    	for(int i = 0; i < transactions.size(); i++){
    		Transaction t = this.transactions.get(i);
    		if(i > 0){
    			cur = t.getDate();
    			interval = getDateDiff(prev, cur, TimeUnit.DAYS);
    			if(interval <= 10 && transactions.get(i - 1).getAmount()<0){//If previous is withdrawal and the interval is less than 10 days
    				accumulate = balance * interval * 0.001 / 365;
    			} else {
    				accumulate = 1 + (balance - 1000) * interval * annual_rate / 365;
    			}
    			interest += accumulate;
    			prev = cur;
    		}
    		balance += t.getAmount(); //We need to process balance before moving towards next step.
    		if(i == transactions.size() - 1){
    			//If there is only one transaction or we've iterated through the final transaction
    			//We need to calculate what interest of that balance, to today
    			cur = new Date();
    			interval = getDateDiff(prev, cur, TimeUnit.DAYS);
    			if(interval <= 10 && transactions.get(i - 1).getAmount()<0){//If previous is withdrawal and the interval is less than 10 days
    				accumulate = balance * interval * 0.001 / 365;
    			} else {
    				accumulate = 1 + (balance - 1000) * interval * annual_rate / 365;
    			}
    			interest += accumulate;
    		}
    	}
    	return interest;
    }

    public double sumTransactions() {
       return checkIfTransactionsExist(true);
    }

    private double checkIfTransactionsExist(boolean checkAll) {
        double amount = 0.0;
        for (Transaction t: transactions)
            amount += t.amount;
        return amount;
    }

    public int getAccountType() {
        return accountType;
    }
    
    //I added another function of get balance.
    public double getBalance(){
    	return balance;
    }
    
    //Transfer money between accounts
    //Suppose the transfer includes transfer money between checking, saving and maxi-saving,
    //within the same bank, or different banks
    //Another account is the account where the money goes to
    //Basic assumptions based on common sense:
    //1. The amount should be greater than 0.
    //2. any account can not transfer money from and to itself
    public void send(Account another, double amount){
    	if(this.equals(another)){
    		throw new IllegalArgumentException("the source and destination accounts should not be the same");
    	} else if (amount <= 0) {
            throw new IllegalArgumentException("source amount must be greater than zero");
        } else {
        	if(balance < amount){
        		throw new IllegalArgumentException("source account not enough balance");
        	} else {
        		this.withdraw(amount);
        		transactions.add(new Transaction(-amount));
        		another.deposit(amount);
        		another.transactions.add(new Transaction(amount));
        	}
        }
    	
    }
    
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

}
