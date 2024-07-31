package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.Account;
import com.openclassrooms.PayMyBuddy.model.DBUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(MoneyService.class)
public class MoneyServiceTest {
    @Autowired
    private MoneyService moneyService;

    @MockBean
    private AccountService accountService;

    @MockBean
    private DBUserService dbUserService;

    @Test
    public void testDeposit() {
        Double depositAmount = 50D;
        Double initialBalance = 100D;
        Double expectedBalance = initialBalance + depositAmount;

        DBUser currentUser = new DBUser();
        currentUser.setUsername("test@email.com");

        Account userAccount = new Account();
        userAccount.setDBUser(currentUser);
        userAccount.setBalance(initialBalance);

        currentUser.setAccount(userAccount);

        when(dbUserService.getUserByUsername("test@email.com")).thenReturn(currentUser);
        when(accountService.saveAccount(any(Account.class))).thenReturn(userAccount);

        moneyService.deposit(currentUser.getUsername(), depositAmount);

        assertEquals(expectedBalance, userAccount.getBalance());
        verify(accountService).saveAccount(userAccount);
    }

    @Test
    public void testWithdrawalSuccess() {
        Double withdrawalAmount = 50D;
        Double initialBalance = 100D;
        Double expectedBalance = initialBalance - withdrawalAmount;

        DBUser currentUser = new DBUser();
        currentUser.setUsername("test@email.com");

        Account userAccount = new Account();
        userAccount.setDBUser(currentUser);
        userAccount.setBalance(initialBalance);

        currentUser.setAccount(userAccount);

        when(dbUserService.getUserByUsername("test@email.com")).thenReturn(currentUser);
        when(accountService.saveAccount(any(Account.class))).thenReturn(userAccount);

        boolean result = moneyService.withdrawal(currentUser.getUsername(), withdrawalAmount);

        assertEquals(expectedBalance, userAccount.getBalance());
        assertTrue(result);
        verify(accountService).saveAccount(userAccount);
    }

    @Test
    public void testWithdrawalInsufficientFunds() {
        Double withdrawalAmount = 150D;
        Double initialBalance = 100D;

        DBUser currentUser = new DBUser();
        currentUser.setUsername("test@email.com");

        Account userAccount = new Account();
        userAccount.setDBUser(currentUser);
        userAccount.setBalance(initialBalance);

        currentUser.setAccount(userAccount);

        when(dbUserService.getUserByUsername("test@email.com")).thenReturn(currentUser);

        boolean result = moneyService.withdrawal(currentUser.getUsername(), withdrawalAmount);

        assertEquals(initialBalance, userAccount.getBalance());
        assertFalse(result);
        verify(accountService, never()).saveAccount(any(Account.class));
    }

}
