package service;

import domain.Account;
import domain.Transaction;
import domain.Type;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface BankService {
    String accountOpen(String name,String email,String accountType);
    List<Account> listAccounts();

    void deposit(String accountNumber, Double amount, String note);


    void withdraw(String accountNumber, Double amount, String withdraw);

    void transfer(String from, String to, Double amount, String note);

    List<Transaction> getStatment(String account);

    List<Account> searchAccountByCustomerName(String q);

    void checkBalance(String acc) throws AccountNotFoundException;
}
