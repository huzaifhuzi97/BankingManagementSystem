package aa;

import domain.Type;
import service.BankService;
import service.service.impl.BankServiceImpl;

import javax.security.auth.login.AccountNotFoundException;
import java.sql.SQLOutput;
import java.util.Scanner;
public class Main {
    static void main() throws AccountNotFoundException {
        Scanner scanner = new Scanner(System.in);
        BankService bankService = new BankServiceImpl();
        System.out.println("Welcome to console Bank");
        Boolean running =true;
        while(running) {
            System.out.println("""
                    1) Open Account.
                    2) Deposit.
                    3) Withdraw.
                    4) Transfer.
                    5) Account Statement.
                    6) List Account.
                    7) Search Accounts by Customer Name.
                    8) Check balance.
                    0) Exit
                    
                    """);
            System.out.println("Choose:");
            String choice = scanner.nextLine().trim();
            System.out.println("Choice:" + choice);
            switch (choice){
                case "1"->openAccount(scanner,bankService);
                case "2"->deposite(scanner,bankService);
                case "3"->withdraw(scanner,bankService);
                case "4"->transfer(scanner,bankService);
                case "5"->statement(scanner,bankService);
                case "6"->listAccounts(scanner,bankService);
                case "7"->seachAccounts(scanner,bankService);
                case "8"->CheckBalance(scanner,bankService);
                case "0"-> running=false;

        }
    }



    }
    private static void CheckBalance(Scanner scanner, BankService bankService) throws AccountNotFoundException {
        System.out.println("Enter the account number: ");
        String acc= scanner.nextLine().trim();
        bankService.checkBalance(acc);

    }

    private static void openAccount(Scanner scanner, BankService bankService) {
        System.out.println("Customer name: ");
        String name=scanner.nextLine().trim();

        System.out.println("Customer email: ");
        String email=scanner.nextLine().trim();

        System.out.println("Account Type (SAVING/CURRENT): ");
        String type=scanner.nextLine().trim();

        System.out.println("Initail deposit (optional,blank for 0): ");
        String amountStr=scanner.nextLine().trim();
        Double initial = Double.valueOf(amountStr);

        String accountNumber= bankService.accountOpen(name,email,type);

        if(initial >0){
            bankService.deposit(accountNumber,initial,"Initial Deposite");
        }

        System.out.println("Account opened: "+ accountNumber);

    }

    private static void deposite(Scanner scanner,BankService bankService) {
        System.out.println("Account number: ");
        String accountNumber=scanner.nextLine().trim();
        System.out.println("Enter amount: ");
        Double amount=scanner.nextDouble();
        bankService.deposit(accountNumber,amount,"Deposit");
        System.out.println("Deposit Successful!");

    }

    private static void withdraw(Scanner scanner,BankService bankService) {
        System.out.println("Account number: ");
        String accountNumber=scanner.nextLine().trim();
        System.out.println("Enter amount: ");
        Double amount=scanner.nextDouble();
        bankService.withdraw(accountNumber,amount,"Withdraw");
        System.out.println("Withdraw Successful!");
    }

    private static void transfer(Scanner scanner,BankService bankService) {
        System.out.println("From Account");
        String from=scanner.nextLine().trim();
        System.out.println("To Account");
        String to =scanner.nextLine().trim();
        System.out.println("Amount: ");
        Double amount =Double.valueOf(scanner.nextLine().trim());
        bankService.transfer(from,to,amount , "TRANSFER_IN");
    }

    private static void statement(Scanner scanner,BankService bankService) {
        System.out.println("Account Number: ");
        String account = scanner.nextLine().trim();
        bankService.getStatment(account).forEach(t ->
        {
            System.out.println(t.getTimeStamp() +
                    " | " + t.getType() + " | " + t.getAmount() + " | " + t.getNote());
        });


    }

    private static void listAccounts(Scanner scanner, BankService bankService) {

        bankService.listAccounts().forEach(a -> {
            System.out.println(
                    a.getAccountNumber() + " | " + a.getAccountType()
            );
        });

    }

    private static void seachAccounts(Scanner scanner,BankService bankService) {
        System.out.println("Customer name contains: ");
        String q = scanner.nextLine().trim();
        bankService.searchAccountByCustomerName(q).forEach(account ->
                System.out.println(account.getAccountNumber()+" | "+account.getAccountType()+" | "+account.getBalance()));

    }




}
