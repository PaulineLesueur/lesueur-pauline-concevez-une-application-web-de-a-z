package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.DBUser;
import com.openclassrooms.PayMyBuddy.service.DBUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private DBUserService dbUserService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/createAccount")
    public String createAccountPage() { return "createAccount"; }

    @PostMapping("/createAccount")
    public String signUp(Model model, @RequestParam String email, @RequestParam String password, @RequestParam String firstName, @RequestParam String lastName, RedirectAttributes redirectAttributes) {
        String createdUser = dbUserService.signUp(email, password, firstName, lastName);

        if("Account created successfully!".equals(createdUser)) {
            redirectAttributes.addFlashAttribute("successSignUpMessage", "Congratulations! Your account is now set up. Please log in to continue !");
            return "redirect:/home/transfer";

        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "The email provided is already associated with an account...");
            return "redirect:/createAccount";
        }
    }



}
