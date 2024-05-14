package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.service.DBUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    private DBUserService userService;

    @GetMapping("/home/transfer")
    public String homePage() {
        return "transfer";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

}
