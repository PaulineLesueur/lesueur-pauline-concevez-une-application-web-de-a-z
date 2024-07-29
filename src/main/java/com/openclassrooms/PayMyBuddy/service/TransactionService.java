package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.constants.Fee;
import com.openclassrooms.PayMyBuddy.model.Account;
import com.openclassrooms.PayMyBuddy.model.DBUser;
import com.openclassrooms.PayMyBuddy.model.Transaction;
import com.openclassrooms.PayMyBuddy.repository.AccountRepository;
import com.openclassrooms.PayMyBuddy.repository.DBUserRepository;
import com.openclassrooms.PayMyBuddy.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private DBUserService dbUserService;

    @Autowired
    private DBUserRepository dbUserRepository;

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

        transaction.setGiverAccount(giverAccount.getId());
        transaction.setReceiverAccount(receiverUser.get().getFirstName());
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transaction.setFee(Fee.TRANSACTION_FEE);
        saveTransaction(transaction);

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

        return true;
    }

}
