package service.service.impl;

import domain.Account;
import domain.Customer;
import domain.Transaction;
import domain.Type;
import repository.AccountRepository;
import repository.CustomerRepository;
import repository.TransactionRepository;
import service.BankService;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService {
    private final AccountRepository accountRepository = new AccountRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();
    private final CustomerRepository customerRepository= new CustomerRepository();




    @Override
    public String accountOpen(String name, String email, String accountType ) {

        String customerId= UUID.randomUUID().toString();
        String accountNumber = getAccountNumber();
        Customer customer = new Customer(email,customerId,name);
        customerRepository.save(customer);

        Account account = new Account(accountNumber,accountType, (double) 0,customerId);
        //save
        accountRepository.save(account);

        return accountNumber;
    }

    @Override
    public List<Account> listAccounts() {
        return accountRepository.findAll();
                // .stream()
             //   .sorted(Comparator.comparing(Account::getAccountNumber))
            //    .collect(Collectors.toList());
    }

    @Override
    public void deposit(String accountNumber, Double amount, String note) {
        Account account=accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " +accountNumber));

        account.setBalance(account.getBalance() + amount);
        System.out.println("DEPOSIT SUCCESSFULL "+amount);

        Transaction transaction = new Transaction(account.getAccountNumber(),
        amount,UUID.randomUUID().toString(),note, LocalDateTime.now(), Type.DEPOSIT);

        transactionRepository.add(transaction);


    }



    private String getAccountNumber() {
        int size = accountRepository.findAll().size() + 1;
        return String.format("AC%06d",size);
    }
    public void withdraw(String accountNumber,Double amount,String note){
        Account account=accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " +accountNumber));
        if(account.getBalance().compareTo(amount) < 0)
            throw new RuntimeException("Insufficient Balance");
        account.setBalance(account.getBalance() - amount);

        Transaction transaction = new Transaction(account.getAccountNumber(),
                amount,UUID.randomUUID().toString(),note, LocalDateTime.now(), Type.WITHDRAW);

        transactionRepository.add(transaction);
    }



    @Override
    public void transfer(String fromAcc, String toAcc, Double amount, String note) {
        if(fromAcc.equals(toAcc))
            throw new RuntimeException("Cannot transfer to your own account");

        Account from=accountRepository.findByNumber(fromAcc)
                .orElseThrow(()->new RuntimeException("Account not found:"+fromAcc));
        Account to =accountRepository.findByNumber(toAcc)
                .orElseThrow(() -> new RuntimeException("Account not found"+ toAcc));
        if(from.getBalance().compareTo(amount) < 0)
            throw new RuntimeException("Insufficient balance: ");

        //balance deducted from and add balance to:

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);
        /// transaction adding in transaction repository
        Transaction fromTransaction= new Transaction(from.getAccountNumber(),amount,UUID.randomUUID().toString(),note,LocalDateTime.now(),Type.TRANSFER_OUT);
        transactionRepository.add(fromTransaction);
        Transaction toTransaction = new Transaction(to.getAccountNumber(),amount,UUID.randomUUID().toString(),note,LocalDateTime.now(),Type.TRANSFER_IN);

    }

    @Override
    public List<Transaction> getStatment(String account) {
       return transactionRepository.findByAccount(account).stream()
               .sorted(Comparator.comparing(Transaction::getTimeStamp))
               .collect(Collectors.toList());

    }

    @Override
    public List<Account> searchAccountByCustomerName(String q) {
        String query= (q==null) ? "":q.toLowerCase();
        List<Account> result = new ArrayList<>();
        for(Customer c: customerRepository.findAll()){
            if(c.getName().toLowerCase().contains(query))
                result.addAll(accountRepository.findByCustomerId(c.getId()));

        }
        result.sort(Comparator.comparing(Account::getAccountNumber));

        return result;
    }

    @Override
    public void checkBalance(String acc) throws AccountNotFoundException {
        Optional<Account> optionalAccount=accountRepository.findByNumber(acc);
        if(optionalAccount.isPresent()){
            Account account=optionalAccount.get();
            System.out.println("Balance : "+account.getBalance());


        }
        else {
            throw new AccountNotFoundException("Account doesn't exists:");



        }

    }
}
