package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.Account;
import com.openclassrooms.PayMyBuddy.model.DBUser;
import com.openclassrooms.PayMyBuddy.model.Transaction;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ConnectionController {

    @Autowired
    private DBUserService dbUserService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/home/transfer")
    public String getConnectionsAndTransactions(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String loggedInUsername = userDetails.getUsername();
        DBUser currentUser = dbUserService.getUserInformations(loggedInUsername);

        Account userAccount = currentUser.getAccount();
        List<Transaction> transactionList = userAccount.getTransactions();

        model.addAttribute("transactions", transactionList);
        model.addAttribute("balance", new DecimalFormat("##.##").format(userAccount.getBalance()));
        model.addAttribute("connections", currentUser.getConnections());
        model.addAttribute("user", currentUser);
        return "transfer";
    }

    @GetMapping("/home/transfer/addConnection")
    public String addConnectionPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String loggedInUsername = userDetails.getUsername();
        DBUser currentUser = dbUserService.getUserInformations(loggedInUsername);
        Account userAccount = currentUser.getAccount();

        model.addAttribute("balance", new DecimalFormat("##.##").format(userAccount.getBalance()));
        model.addAttribute("user", currentUser);

        return "addConnection";
    }

    @PostMapping("/addConnection")
    public String addConnection(Model model, @AuthenticationPrincipal UserDetails userDetails, @RequestParam String email, RedirectAttributes redirectAttributes) {
        String loggedInUsername = userDetails.getUsername();
        /*String connectionAdded = dbUserService.addConnection(loggedInUsername, email);*/

        boolean success = dbUserService.addConnection(loggedInUsername, email);

        if(success) {
            redirectAttributes.addFlashAttribute("successMessage", "Connection added successfully !");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found or already connected");
            return "redirect:/home/transfer/addConnection";
        }

        return "redirect:/home/transfer";
    }

}
