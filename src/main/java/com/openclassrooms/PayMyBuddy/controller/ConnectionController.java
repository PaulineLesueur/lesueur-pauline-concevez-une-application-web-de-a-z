package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.DBUser;
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

import java.util.ArrayList;
import java.util.List;

@Controller
public class ConnectionController {

    @Autowired
    private DBUserService dbUserService;

    @GetMapping("/home/transfer")
    public String getConnectionList(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String loggedInUsername = userDetails.getUsername();
        DBUser currentUser = dbUserService.getConnectionList(loggedInUsername);

        model.addAttribute("connections", currentUser.getConnections());
        model.addAttribute("user", currentUser);
        return "transfer";
    }

    @GetMapping("/home/transfer/addConnection")
    public String addConnectionPage() { return "addConnection"; }

    @PostMapping("/addConnection")
    public String addConnection(Model model, @AuthenticationPrincipal UserDetails userDetails, @RequestParam String email, RedirectAttributes redirectAttributes) {
        String loggedInUsername = userDetails.getUsername();
        String connectionAdded = dbUserService.addConnection(loggedInUsername, email);

        if("User not found".equals(connectionAdded)) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found");
            return "redirect:/home/transfer/addConnection";
        } else if("You're already connected with this user".equals(connectionAdded)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You're already connected with this user");
            return "redirect:/home/transfer/addConnection";

        } else {
            redirectAttributes.addFlashAttribute("successMessage", "Connection added successfully !");
        }

        return "redirect:/home/transfer";
    }

}
