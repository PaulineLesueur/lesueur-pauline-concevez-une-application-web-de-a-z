package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.DBUser;
import com.openclassrooms.PayMyBuddy.service.DBUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        Long userId = dbUserService.getUserByUsername(loggedInUsername).getId();

        List<DBUser> userConnections = currentUser.getConnections();
        List<DBUser> connectionList = dbUserService.getConnectionsOfUserById(userId);

        for(DBUser connection : connectionList) {
            if(!userConnections.contains(connection)) {
                userConnections.add(connection);
            }
        }

        currentUser.setConnections(userConnections);

        model.addAttribute("connections", currentUser.getConnections());
        return "transfer";
    }

    @GetMapping("/home/transfer/addConnection")
    public String addConnection() { return "addConnection"; }

}
