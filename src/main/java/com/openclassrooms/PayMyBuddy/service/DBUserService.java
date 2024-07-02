package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.DBUser;
import com.openclassrooms.PayMyBuddy.repository.DBUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    public DBUser saveUser(DBUser user) {
        return dbUserRepository.save(user);
    }

    public DBUser signUp(String email, String password, String firstName, String lastName) {
        DBUser newUser = new DBUser();
        newUser.setUsername(email);

        DBUser userToCheck = getUserByUsername(email);
        if(userToCheck == null) {
            newUser.setPassword(passwordEncoder().encode(password));
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setRole("USER");

            saveUser(newUser);

            return newUser;

        } else {
            return null;
        }
    }

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public DBUser getConnectionList(String username) {
        DBUser currentUser = getUserByUsername(username);
        Long userId = currentUser.getId();

        List<DBUser> userConnections = currentUser.getConnections();
        List<DBUser> connectionList = getConnectionsOfUserById(userId);

        for(DBUser connection : connectionList) {
            if(!userConnections.contains(connection)) {
                userConnections.add(connection);
            }
        }

        currentUser.setConnections(userConnections);
        saveUser(currentUser);

        return currentUser;
    }

    public String addConnection(String username, String email) {
        DBUser currentUser = getUserByUsername(username);
        List<DBUser> userConnections = currentUser.getConnections();

        DBUser connectionToAdd = getUserByUsername(email);
        if(connectionToAdd == null) {
            return "User not found";
        } else if(userConnections.contains(connectionToAdd)) {
            return "You're already connected with this user";

        } else {
            userConnections.add(connectionToAdd);
            currentUser.setConnections(userConnections);
            saveUser(currentUser);
            return "Connection added successfully !";
        }
    }

}
