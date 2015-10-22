package com.abc;

import java.text.ParseException;
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
    
    public void lastYear() {
    	//For test use only. As interests are accrued by day, 
    	//need to find out a way to change transaction date
    	//Otherwise the interval will always be 0;
    	long year = 24L*3600L*1000L*365L;
    	transactionDate= new Date(transactionDate.getTime() - year);
    }
    
    public void tenDaysAgo(){
    	//For test use only. As interests are accrued by day, 
    	//need to find out a way to change transaction date
    	//Otherwise the interval will always be 0;
    	long tenDays = 24L*3600L*1000L*10L;
    	transactionDate= new Date(transactionDate.getTime() - tenDays);
    }
    
    public void hundredDaysAgo(){
    	//For test use only. As interests are accrued by day, 
    	//need to find out a way to change transaction date
    	//Otherwise the interval will always be 0;
    	long hundredDays = 24L*3600L*1000L*100L;
    	transactionDate= new Date(transactionDate.getTime() - hundredDays);
    }

}
