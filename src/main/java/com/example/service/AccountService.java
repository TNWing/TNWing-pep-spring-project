package com.example.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;


    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    public Account createAccount(Account account){
        return accountRepository.save(account);
    }

    public Account doesAccountWithUsernameExist(String username){
        return accountRepository.findAccountByUsername(username);
    }

    public Account getAccountFromID(int id){
        return accountRepository.findAccountByAccountId(id);
    }

    public Account verifyAccount(String username,String password){
        return accountRepository.findAccountByUsernameAndPassword(username,password);
    }
}
