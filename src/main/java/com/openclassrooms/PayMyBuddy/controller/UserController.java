package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.Account;
import com.openclassrooms.PayMyBuddy.model.DBUser;
import com.openclassrooms.PayMyBuddy.service.AccountService;
import com.openclassrooms.PayMyBuddy.service.DBUserService;
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

@Controller
public class UserController {

    @Autowired
    private DBUserService dbUserService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/createAccount")
    public String createAccountPage() { return "createAccount"; }

    @PostMapping("/createAccount")
    public String signUp(Model model, @RequestParam String email, @RequestParam String password, @RequestParam String firstName, @RequestParam String lastName, RedirectAttributes redirectAttributes) {
        DBUser createdUser = dbUserService.signUp(email, password, firstName, lastName);

        if(createdUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "The email provided is already associated with an account...");
            return "redirect:/createAccount";

        } else {
            accountService.createAccount(createdUser);
            return "redirect:/home/transfer";
        }
    }

    @GetMapping("/home/money")
    public String moneyPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String loggedInUsername = userDetails.getUsername();
        DBUser currentUser = dbUserService.getUserByUsername(loggedInUsername);
        Account userAccount = currentUser.getAccount();

        model.addAttribute("balance", new DecimalFormat("##.##").format(userAccount.getBalance()));
        model.addAttribute("user", currentUser);

        return "money";
    }

}
