package com.banking_application;

import java.util.*;
public class BankingConsoleApplication{
	public static void main(String[] args) throws Exception{
		Scanner sc = new Scanner(System.in);
		Map<Integer,SavingsAccount> customers = new HashMap<>();
		System.out.println("Welcome to 'BANK OF COGNIZANT'");
		while(true) {
			System.out.println("\n1. Set Annual Interest Rate");
			System.out.println("2. Check Annual Interest Rate");
			System.out.println("3. Check your Balance");
			System.out.println("4. Create new Account");
			System.out.println("5. Calculate your Monthly Interest");
			System.out.println("6. How we calculate the Interest");
			System.out.println("7. About Us");
			System.out.println("8. Exit");
			System.out.println("Enter your choice");
			
			switch(Integer.parseInt(sc.nextLine())){
				case 1: if(SavingsAccount.getAnnualInterestRate() != 0) {
							System.out.println("Annual Interest Rate is " + SavingsAccount.getAnnualInterestRate() + "%");
						}
						System.out.println("Enter the Annual Interest Rate you want to set");
						double interestRate=Double.parseDouble(sc.nextLine());
						if(interestRate <= 0) {
							System.out.println("Annual Interest Rate can be neither zero nor negative.\nPlease Try again");
							break;
						}
						SavingsAccount.modifyInterestRate(interestRate);
						System.out.println("Updated Annual Interest Rate is " + SavingsAccount.getAnnualInterestRate() + "%");
						break;
				case 2: System.out.println("Annual Interest Rate is " + SavingsAccount.getAnnualInterestRate() + "%");
						break;
				case 3: System.out.println("Enter your Account Number");
						int accountNumber = Integer.parseInt(sc.nextLine());
						if(!customers.containsKey(accountNumber)) {
							System.out.println("Sorry you don't have an account in our bank!\nKindly create a new account");
							break;
						}
						System.out.println("Hi "+customers.get(accountNumber).getAccountHolderName()+", Welcome to our application");
						System.out.printf("Your balance is ₹%.2f",customers.get(accountNumber).getSavingsBalance());
						System.out.println();
						break;
				case 4: System.out.println("Enter Your Name");
						String accountHolderName = sc.nextLine();
						if(!accountHolderName.matches("[a-zA-z\s]+")) {
							System.out.println("Name can contain only Alphabets and Spaces\nPlease Try again!");
							break;
						}
						System.out.println("Enter your opening balance");
						double balance = Double.parseDouble(sc.nextLine());
						if(balance <= 0) {
							System.out.println("Balance can be neither zero nor negative.\nPlease Try again");
							break;
						}
						SavingsAccount ac = new SavingsAccount(accountHolderName,balance);
						customers.put(ac.getAccountNumber(),ac );
						System.out.println("Your account has been created successfully!");
						System.out.println("Account Number : "+ac.getAccountNumber());
						System.out.println("Account Holder's Name : "+ac.getAccountHolderName());
						System.out.println("Opening Balance : ₹" + ac.getSavingsBalance());
						break;
				case 5: if(SavingsAccount.getAnnualInterestRate()==0) {
							System.out.println("Please set Annual Interest Rate first!");
							break;
						}
						System.out.println("Enter your Account Number");
						accountNumber = Integer.parseInt(sc.nextLine());
						if(!customers.containsKey(accountNumber)) {
							System.out.println("Sorry you don't have an account in our bank!\nKindly create a new account.");
							break;
						}
						System.out.println("Hi "+customers.get(accountNumber).getAccountHolderName());
						System.out.printf("Your Monthly Interest is ₹%.2f",customers.get(accountNumber).calculateMonthlyInterestWithoutUpdate());
						System.out.println("\nDo you want to update your balance with the interest amount?(Yes/No)");
						String choice = sc.nextLine();
						if(choice.equalsIgnoreCase("Yes")) {
							customers.get(accountNumber).calculateMonthlyInterest();
							System.out.println("Your balance has been updated successfully.");
						}
						else if(choice.equalsIgnoreCase("no")) {
							break;
						}
						else {
							System.out.println("Invalid Input");
						}
						break;
				case 6: System.out.println("Welcome to the Bank of Cognizant!\nOur mission is to provide transparent and efficient banking services to our customers\nHere's a brief overview of how we operate:\n");
						System.out.println("Annual Interest Rate: Initially, the annual interest rate is set to 0.\nYou set the rate of interest first to see the 'Interest' or to 'Update balance with Interest'.\nThe interest rate is divided by 12 to calculate the monthly interest rate.\nThis monthly interest rate is then multiplied by your account balance to determine the interest earned each month.\n");
						System.out.println("Interest Calculation: Each month, the interest you will earn is shown, ensuring that your savings grow steadily over time\n\nUpdate Balance with Interest: Our system allows you to update your balance with the interest earned for the month, providing an accurate and up-to-date account balance.\nWe are committed to offering you the best banking experience with clear and straightforward processes.");
						break;
				case 7: System.out.println("Meet the team behind Ctrl+Alt+Defeat, the creators of the Bank of Cognizant:\nSankeerth\nSathya\nAbhigyan\nRohit Mishra\nWe are proud to present our project and hope you find our banking application useful and user-friendly.\nThank you for choosing the Bank of Cognizant!");
						break;
				case 8: System.out.println("Thanks for using our application.");
						return;
				default:System.out.println("You chose an invalid option, Please try again");
			}
			System.out.println("_________________________________________________________________________________________________________");
			Thread.sleep(750);
		}
	
	}
}
