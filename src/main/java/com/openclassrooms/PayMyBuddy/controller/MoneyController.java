package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.service.MoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MoneyController {

    @Autowired
    private MoneyService moneyService;

    @PostMapping("/deposit")
    public String deposit(Model model, @RequestParam Double amount, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        String loggedInUsername = userDetails.getUsername();

        moneyService.deposit(loggedInUsername, amount);

        redirectAttributes.addFlashAttribute("successMessage", "Deposit performed successfully !");
        return "redirect:/home/money";
    }

    @PostMapping("/withdrawal")
    public String withdrawal(Model model, @RequestParam Double amount, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        String loggedInUsername = userDetails.getUsername();
        boolean success = moneyService.withdrawal(loggedInUsername, amount);

        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Withdrawal performed successfully !");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Payment failed : Insufficient account balance.");
        }

        return "redirect:/home/money";
    }

}
