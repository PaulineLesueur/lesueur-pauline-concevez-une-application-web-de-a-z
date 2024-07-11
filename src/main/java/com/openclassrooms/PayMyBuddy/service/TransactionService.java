package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.constants.Fee;
import com.openclassrooms.PayMyBuddy.model.Account;
import com.openclassrooms.PayMyBuddy.model.DBUser;
import com.openclassrooms.PayMyBuddy.model.Transaction;
import com.openclassrooms.PayMyBuddy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private DBUserService dbUserService;

    public Transaction saveTransaction(Transaction transaction) { return transactionRepository.save(transaction); }

    public boolean transfer(String giverUsername, Long receiverId, String description, Double amount) {
        DBUser currentUser = dbUserService.getUserByUsername(giverUsername);
        Optional<DBUser> receiverUser = dbUserService.getUserById(receiverId);

        if(receiverUser.isEmpty()) {
            return false; //receiver doesn't exist
        }

        Account giverAccount = currentUser.getAccount();
        Account receiverAccount = receiverUser.get().getAccount();
        Transaction transaction = new Transaction();

        transaction.setGiverAccount(giverAccount);
        transaction.setReceiverAccount(receiverAccount);
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transaction.setFee(Fee.TRANSACTION_FEE);

        Double totalAmountWithFee = amount + (amount * Fee.TRANSACTION_FEE);

        if(giverAccount.getBalance() < totalAmountWithFee) {
            return false; //insufficient funds
        }

        Double newGiverBalance = giverAccount.getBalance() - totalAmountWithFee;
        Double newReceiverBalance = receiverAccount.getBalance() + amount;

        giverAccount.setBalance(newGiverBalance);
        receiverAccount.setBalance(newReceiverBalance);

        accountService.saveAccount(giverAccount);
        accountService.saveAccount(receiverAccount);

        return true; //success
    }

}
