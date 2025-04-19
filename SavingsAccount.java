package com.banking_application;

public class SavingsAccount extends Account{
	private static double annualInterestRate;
	private double savingsBalance;
	
	
	public SavingsAccount(String accountHolderName,double savingsBalance){
		super(accountHolderName);
		this.savingsBalance = savingsBalance;
	}
	
	
	//Getter and Setter Methods
	public static double getAnnualInterestRate() {
		return annualInterestRate;
	}
	public static void setAnnualInterestRate(double annualInterestRate) {
		SavingsAccount.annualInterestRate = annualInterestRate;
	}

	public double getSavingsBalance() {
		return savingsBalance;
	}
	public void setSavingsBalance(double savingsBalance) {
		this.savingsBalance = savingsBalance;
	}
	
	
	
	
	//Method to Modify Interest Rate
	public static void modifyInterestRate(double newAnnualInterestRate) {
		SavingsAccount.annualInterestRate = newAnnualInterestRate;
	}
	
	

	//Method to just calculate Monthly Interest without updating Savings balance
	public double calculateMonthlyInterestWithoutUpdate() {
		double monthlyInterestRate = (annualInterestRate/100) / 12.0;
		double monthlyInterest = this.getSavingsBalance() * monthlyInterestRate;
		return monthlyInterest;
	}
	
	
	//Method to Calculate Monthly Interest and Update Savings Balance
	public double calculateMonthlyInterest(){
		double monthlyInterest = calculateMonthlyInterestWithoutUpdate();
		this.setSavingsBalance(this.getSavingsBalance() + monthlyInterest);
		return monthlyInterest;
	}
}
