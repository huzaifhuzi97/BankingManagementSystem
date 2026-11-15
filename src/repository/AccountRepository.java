package repository;

import domain.Account;
import domain.Customer;

import java.util.*;

public class AccountRepository {

    private  final Map<String, Account> accountsByNumber= new HashMap<>();// [ account number --> account object ]
                    // key      value
    public void save(Account account){

        accountsByNumber.put(account.getAccountNumber(),account);
    }

    public List<Account> findAll() {  //done

        return new ArrayList<>(accountsByNumber.values());



    }

    public Optional<Account> findByNumber(String accountNumber) {//done

        return Optional.ofNullable(accountsByNumber.get(accountNumber));

    }

    public List<Account> findByCustomerId(String customerId) {//done
        List<Account> result= new ArrayList<>();
        for(Account a: accountsByNumber.values()){
            if(a.getCustomerId().equals(customerId))
                result.add(a);

        }
        return result;
    }
}
