package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.DBUser;
import com.openclassrooms.PayMyBuddy.repository.DBUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(DBUserService.class)
public class DBUserServiceTest {
    @Autowired
    DBUserService dbUserService;

    @MockBean
    private DBUserRepository dbUserRepository;

    public static List<DBUser> connectionList = new ArrayList<>();

    static {
        connectionList.add(new DBUser(2L, "test2@email.com", "password", "firstname2", "lastname2", "USER", null, null));
        connectionList.add(new DBUser(3L, "test3@email.com", "password", "firstname3", "lastname3", "USER", null, null));
    }

    @Test
    public void testGetUserByUsername() {
        DBUser user = new DBUser();
        user.setUsername("test@email.com");
        when(dbUserRepository.findByUsername(any(String.class))).thenReturn(user);
        DBUser userFound = dbUserService.getUserByUsername("test@email.com");
        assertEquals(userFound.getUsername(), user.getUsername());
    }

    @Test
    public void testGetConnectionsOfUserById() {
        when(dbUserRepository.findConnectionsByUserId(any(Long.class))).thenReturn(connectionList);
        List<DBUser> connectionsFound = dbUserService.getConnectionsOfUserById(1L);
        assertEquals(connectionsFound, connectionList);
    }

    @Test
    public void testSaveUser() {
        DBUser userToSave = new DBUser();
        userToSave.setUsername("test1@email.com");
        userToSave.setPassword("password");
        userToSave.setFirstName("firsname1");
        userToSave.setLastName("lasname1");
        userToSave.setRole("USER");
        userToSave.setConnections(connectionList);

        when(dbUserRepository.save(any(DBUser.class))).thenReturn(userToSave);
        DBUser savedUser = dbUserService.saveUser(userToSave);

        assertEquals(savedUser.getUsername(), userToSave.getUsername());
        assertEquals(savedUser.getPassword(), userToSave.getPassword());
        assertEquals(savedUser.getFirstName(), userToSave.getFirstName());
        assertEquals(savedUser.getLastName(), userToSave.getLastName());
        assertEquals(savedUser.getRole(), userToSave.getRole());
        assertEquals(savedUser.getConnections(), userToSave.getConnections());
    }

    @Test
    public void testAddConnection() {

    }
}
