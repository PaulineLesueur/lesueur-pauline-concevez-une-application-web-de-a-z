package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.Account;
import com.openclassrooms.PayMyBuddy.model.DBUser;
import com.openclassrooms.PayMyBuddy.model.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoneyService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private DBUserService dbUserService;

    public void deposit(String username, Double amount) {
        DBUser currentUser = dbUserService.getUserByUsername(username);
        Account userAccount = currentUser.getAccount();
        Money deposit = new Money();

        deposit.setAmount(amount);
        Double newBalance = userAccount.getBalance() + deposit.getAmount();
        userAccount.setBalance(newBalance);
        accountService.saveAccount(userAccount);
    }

    public boolean withdrawal(String username, Double amount) {
        DBUser currentUser = dbUserService.getUserByUsername(username);
        Account userAccount = currentUser.getAccount();
        Money withdrawal = new Money();

        withdrawal.setAmount(amount);

        if (userAccount.getBalance() >= amount) {
            Double newBalance = userAccount.getBalance() - withdrawal.getAmount();
            userAccount.setBalance(newBalance);
            accountService.saveAccount(userAccount);

            return true;
        } else {
            return false;
        }
    }


}
