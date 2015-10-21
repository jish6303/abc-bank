package com.abc;

import java.util.ArrayList;
import java.util.List;

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
        double amount = sumTransactions();
        switch(accountType){
            case SAVINGS:
                if (amount <= 1000)
                    return amount * 0.001;
                else
                    return 1 + (amount-1000) * 0.002;
//            case SUPER_SAVINGS:
//                if (amount <= 4000)
//                    return 20;
            case MAXI_SAVINGS:
                if (amount <= 1000)
                    return amount * 0.02;
                if (amount <= 2000)
                    return 20 + (amount-1000) * 0.05;
                return 70 + (amount-2000) * 0.1;
            default:
                return amount * 0.001;
        }
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

}
