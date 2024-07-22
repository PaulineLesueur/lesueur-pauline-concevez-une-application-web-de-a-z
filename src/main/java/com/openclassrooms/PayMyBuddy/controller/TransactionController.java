package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.Account;
import com.openclassrooms.PayMyBuddy.model.DBUser;
import com.openclassrooms.PayMyBuddy.model.Transaction;
import com.openclassrooms.PayMyBuddy.repository.TransactionRepository;
import com.openclassrooms.PayMyBuddy.service.AccountService;
import com.openclassrooms.PayMyBuddy.service.DBUserService;
import com.openclassrooms.PayMyBuddy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private DBUserService dbUserService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/transfer")
    public String transfer(Model model, @RequestParam Long receiver, @RequestParam String description, @RequestParam Double amount, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        String loggedInUsername = userDetails.getUsername();

        boolean success = transactionService.transfer(loggedInUsername, receiver, description, amount);

        if(success) {
            redirectAttributes.addFlashAttribute("successMessage", "Transfer performed successfully ! Please note that a 0.5% fee has been deducted.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Transfer failed: Insufficient funds or invalid receiver.");
        }

        return "redirect:/home/transfer";
    }

}
