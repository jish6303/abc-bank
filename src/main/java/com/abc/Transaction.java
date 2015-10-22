package com.abc;

import java.util.Calendar;
import java.util.Date;

public class Transaction {
    public final double amount;

    private Date transactionDate;

    public Transaction(double amount) {
        this.amount = amount;
        this.transactionDate = DateProvider.getInstance().now();
    }
    
    //Added getAmount and getDate for each transaction, in order to adjust interest
    public double getAmount(){
    	return amount;
    }
    
    public Date getDate(){
    	return transactionDate;
    }

}
