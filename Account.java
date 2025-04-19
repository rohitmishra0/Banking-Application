package com.banking_application;

public class Account {
	private static int accountNumberGeneratorValue=0;
	protected int accountNumber;
	protected String accountHolderName;
	
	//Parameterized Constructor to Initialize accountNumber
	public Account(String accountHolderName) {
		this.setAccountHolderName(accountHolderName);
		this.setAccountNumber(Account.accountNumberGenerator());
	}
	
	//Simple Mechanism to Generate Unique Account Numbers for each account created
	public static int accountNumberGenerator() {
		return ++Account.accountNumberGeneratorValue;
	}

	
	//Getters and Setters for the variables
	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}
	
}
