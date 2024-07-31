package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.constants.Fee;
import com.openclassrooms.PayMyBuddy.model.Account;
import com.openclassrooms.PayMyBuddy.model.DBUser;
import com.openclassrooms.PayMyBuddy.model.Transaction;
import com.openclassrooms.PayMyBuddy.repository.DBUserRepository;
import com.openclassrooms.PayMyBuddy.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(TransactionService.class)
public class TransactionServiceTest {
    @Autowired
    private TransactionService transactionService;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private AccountService accountService;

    @MockBean
    private DBUserService dbUserService;

    @MockBean
    private DBUserRepository dbUserRepository;

    @Test
    public void testSaveTransaction() {
        Transaction transactionToSave = new Transaction();
        transactionToSave.setGiverAccount(1L);
        transactionToSave.setReceiverAccount("receiver@email.com");
        transactionToSave.setDescription("description");
        transactionToSave.setAmount(10D);
        transactionToSave.setFee(Fee.TRANSACTION_FEE);

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transactionToSave);
        Transaction savedTransaction = transactionService.saveTransaction(transactionToSave);

        assertEquals(savedTransaction.getGiverAccount(), transactionToSave.getGiverAccount());
        assertEquals(savedTransaction.getReceiverAccount(), transactionToSave.getReceiverAccount());
        assertEquals(savedTransaction.getDescription(), transactionToSave.getDescription());
        assertEquals(savedTransaction.getAmount(), transactionToSave.getAmount());
        assertEquals(savedTransaction.getFee(), transactionToSave.getFee());
    }

    @Test
    public void testTransferSuccess() {
        Double transferAmount = 100D;
        Double initialGiverBalance = 200D;
        Double initialReceiverBalance = 50D;
        Double expectedGiverBalance = initialGiverBalance - transferAmount - (transferAmount * Fee.TRANSACTION_FEE);
        Double expectedReceiverBalance = initialReceiverBalance + transferAmount;
        String description = "description";

        DBUser giverUser = new DBUser();
        giverUser.setUsername("giver@email.com");

        Account giverAccount = new Account();
        giverAccount.setDBUser(giverUser);
        giverAccount.setBalance(initialGiverBalance);
        giverUser.setAccount(giverAccount);

        DBUser receiverUser = new DBUser();
        receiverUser.setId(1L);
        receiverUser.setFirstName("Receiver");

        Account receiverAccount = new Account();
        receiverAccount.setDBUser(receiverUser);
        receiverAccount.setBalance(initialReceiverBalance);
        receiverUser.setAccount(receiverAccount);

        when(dbUserService.getUserByUsername("giver@email.com")).thenReturn(giverUser);
        when(dbUserService.getUserById(1L)).thenReturn(Optional.of(receiverUser));
        when(accountService.saveAccount(any(Account.class))).thenReturn(giverAccount);
        when(accountService.saveAccount(any(Account.class))).thenReturn(receiverAccount);

        boolean result = transactionService.transfer("giver@email.com", 1L, description, transferAmount);

        assertTrue(result);
        assertEquals(expectedGiverBalance, giverAccount.getBalance());
        assertEquals(expectedReceiverBalance, receiverAccount.getBalance());
        verify(accountService).saveAccount(giverAccount);
        verify(accountService).saveAccount(receiverAccount);
    }

    @Test
    public void testTransferInsufficientFunds() {
        Double transferAmount = 200D;
        Double initialGiverBalance = 100D;
        String description = "description";

        DBUser giverUser = new DBUser();
        giverUser.setUsername("giver@email.com");

        Account giverAccount = new Account();
        giverAccount.setDBUser(giverUser);
        giverAccount.setBalance(initialGiverBalance);
        giverUser.setAccount(giverAccount);

        DBUser receiverUser = new DBUser();
        receiverUser.setId(1L);
        receiverUser.setFirstName("Receiver");

        Account receiverAccount = new Account();
        receiverAccount.setDBUser(receiverUser);
        receiverAccount.setBalance(0D);
        receiverUser.setAccount(receiverAccount);

        when(dbUserService.getUserByUsername("giver@email.com")).thenReturn(giverUser);
        when(dbUserService.getUserById(1L)).thenReturn(Optional.of(receiverUser));

        boolean result = transactionService.transfer("giver@email.com", 1L, description, transferAmount);

        assertFalse(result);
        assertEquals(initialGiverBalance, giverAccount.getBalance());
        assertEquals(0D, receiverAccount.getBalance());
        verify(accountService, never()).saveAccount(giverAccount);
        verify(accountService, never()).saveAccount(receiverAccount);
    }

    @Test
    public void testTransferReceiverNotFound() {
        Double transferAmount = 100D;
        Double initialGiverBalance = 200D;
        String description = "description";

        DBUser giverUser = new DBUser();
        giverUser.setUsername("giver@email.com");

        Account giverAccount = new Account();
        giverAccount.setDBUser(giverUser);
        giverAccount.setBalance(initialGiverBalance);
        giverUser.setAccount(giverAccount);

        when(dbUserService.getUserByUsername("giver@email.com")).thenReturn(giverUser);
        when(dbUserService.getUserById(1L)).thenReturn(Optional.empty());

        boolean result = transactionService.transfer("giver@email.com", 1L, description, transferAmount);

        assertFalse(result);
        assertEquals(initialGiverBalance, giverAccount.getBalance());
        verify(accountService, never()).saveAccount(giverAccount);
    }

}
