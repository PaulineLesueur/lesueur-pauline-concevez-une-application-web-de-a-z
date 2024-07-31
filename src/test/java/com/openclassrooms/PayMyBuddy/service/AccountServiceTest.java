package com.openclassrooms.PayMyBuddy.service;


import com.openclassrooms.PayMyBuddy.model.Account;
import com.openclassrooms.PayMyBuddy.model.DBUser;
import com.openclassrooms.PayMyBuddy.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(AccountService.class)
public class AccountServiceTest {
    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @Test
    public void testSaveAccount() {
        Account accountToSave = new Account();
        accountToSave.setDBUser(new DBUser());
        accountToSave.setBalance(0D);
        accountToSave.setTransactions(new ArrayList<>());

        when(accountRepository.save(any(Account.class))).thenReturn(accountToSave);
        Account savedAccount = accountService.saveAccount(accountToSave);

        assertEquals(savedAccount.getDBUser(), accountToSave.getDBUser());
        assertEquals(savedAccount.getBalance(), accountToSave.getBalance());
        assertEquals(savedAccount.getTransactions(), accountToSave.getTransactions());
    }

    @Test
    public void testCreateAccount() {
        DBUser user = new DBUser();
        Account newAccount = new Account();
        newAccount.setDBUser(user);
        newAccount.setBalance(0D);
        newAccount.setTransactions(new ArrayList<>());

        when(accountRepository.save(any(Account.class))).thenReturn(newAccount);
        Account createdAccount = accountService.createAccount(user);

        assertEquals(createdAccount.getDBUser(), newAccount.getDBUser());
        assertEquals(createdAccount.getBalance(), newAccount.getBalance());
        assertEquals(createdAccount.getTransactions(), newAccount.getTransactions());
    }
}
