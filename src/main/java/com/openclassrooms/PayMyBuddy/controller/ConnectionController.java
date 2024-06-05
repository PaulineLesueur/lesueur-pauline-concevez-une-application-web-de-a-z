package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.DBUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ConnectionController {

    private static DBUser currentUser = new DBUser(3L, "currentuser@gmail.com", "dehjfv", "USER", null);

    @GetMapping("/connectionList")
    public String getConnectionList(Model model) {
        List<DBUser> connectionList = new ArrayList<>();
        DBUser user1 = new DBUser(1L, "test1@email.com", "cndjknzjk", "USER", null);
        DBUser user2 = new DBUser(2L, "test2@email.com", "cndjkefdz", "USER", null);
        connectionList.add(user1);
        connectionList.add(user2);
        currentUser.setConnections(connectionList);
        model.addAttribute("connections", connectionList);
        return "transfer";
    }

}
