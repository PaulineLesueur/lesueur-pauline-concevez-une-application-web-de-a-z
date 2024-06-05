package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.service.DBUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    private DBUserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

}
