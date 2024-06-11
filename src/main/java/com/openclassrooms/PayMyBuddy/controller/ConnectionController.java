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

    /*private static DBUser currentUser = new DBUser(3L, "currentuser@gmail.com", "dehjfv", "USER", null);*/

    @Autowired
    private DBUserService dbUserService;

    private static List<DBUser> connectionList = new ArrayList<>();

    @GetMapping("/home/transfer")
    public String getConnectionList(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String loggedInUser = userDetails.getUsername();
        Long userId = dbUserService.getUserByUsername(loggedInUser).getId();

        connectionList.addAll(dbUserService.getConnectionsOfUserById(userId));
        model.addAttribute("connections", connectionList);
        return "transfer";
    }

}
