package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.DBUser;
import com.openclassrooms.PayMyBuddy.repository.DBUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DBUserService {

    @Autowired
    private DBUserRepository dbUserRepository;

    public DBUser getUserByUsername(String username) {
        return dbUserRepository.findByUsername(username);
    }

    public List<DBUser> getConnectionsOfUserById(Long userId) {
        return dbUserRepository.findConnectionsByUserId(userId);
    }

}
