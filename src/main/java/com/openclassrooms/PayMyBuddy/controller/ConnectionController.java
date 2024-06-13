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
        DBUser currentUser = dbUserService.getUserByUsername(loggedInUsername);
        Long userId = currentUser.getId();

        List<DBUser> userConnections = currentUser.getConnections();
        List<DBUser> connectionList = dbUserService.getConnectionsOfUserById(userId);

        for(DBUser connection : connectionList) {
            if(!userConnections.contains(connection)) {
                userConnections.add(connection);
            }
        }

        for(DBUser connection : userConnections) {
            if(!userConnections.contains(connection)) {
                userConnections.add(connection);
            }
        }

        currentUser.setConnections(userConnections);

        model.addAttribute("connections", currentUser.getConnections());
        model.addAttribute("user", currentUser);
        return "transfer";
    }

    @GetMapping("/home/transfer/addConnection")
    public String addConnectionPage() { return "addConnection"; }

    @PostMapping("/addConnection")
    public String addConnection(Model model, @AuthenticationPrincipal UserDetails userDetails, @RequestParam String email, RedirectAttributes redirectAttributes) {
        String loggedInUsername = userDetails.getUsername();
        DBUser currentUser = dbUserService.getUserByUsername(loggedInUsername);
        List<DBUser> userConnections = currentUser.getConnections();

        DBUser connectionToAdd = dbUserService.getUserByUsername(email);
        if(connectionToAdd == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found");
            return "redirect:/home/transfer/addConnection";
        } else if(userConnections.contains(connectionToAdd)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You're already connected with this user");
            return "redirect:/home/transfer/addConnection";

        } else if(!userConnections.contains(connectionToAdd)) {
            userConnections.add(connectionToAdd);
            currentUser.setConnections(userConnections);
            dbUserService.saveUser(currentUser);
            redirectAttributes.addFlashAttribute("successMessage", "Connection added successfully !");
        }

        return "redirect:/home/transfer";
    }

}
