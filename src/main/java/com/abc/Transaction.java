package com.abc;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    
    public void lastYear() throws ParseException{//For test use only. As interests are accrued by day, 
    	//need to find out a way to change transaction date
    	//Otherwise the interval will always be 0;
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	transactionDate= dateFormat.parse("2014-10-22");
    }

}
