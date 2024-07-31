package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.DBUser;
import com.openclassrooms.PayMyBuddy.repository.DBUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(DBUserService.class)
public class DBUserServiceTest {
    @Autowired
    DBUserService dbUserService;

    @MockBean
    private DBUserRepository dbUserRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

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
    public void testSignUp() {
        String email = "test@email.com";
        String password = "password";
        String firstName = "John";
        String lastName = "Doe";

        DBUser userToSave = new DBUser();
        userToSave.setUsername(email);
        userToSave.setPassword("password");
        userToSave.setFirstName(firstName);
        userToSave.setLastName(lastName);
        userToSave.setRole("USER");

        when(dbUserRepository.findByUsername(email)).thenReturn(null);
        when(dbUserRepository.save(any(DBUser.class))).thenReturn(userToSave);

        DBUser savedUser = dbUserService.signUp(email, password, firstName, lastName);

        assertNotNull(savedUser);
        assertEquals(userToSave.getUsername(), savedUser.getUsername());
        assertEquals(userToSave.getFirstName(), savedUser.getFirstName());
        assertEquals(userToSave.getLastName(), savedUser.getLastName());
        assertEquals(userToSave.getRole(), savedUser.getRole());
    }

    @Test
    public void testAlreadyRegistered() {
        String email = "existing@email.com";
        String password = "password";
        String firstName = "John";
        String lastName = "Doe";

        DBUser existingUser = new DBUser();
        existingUser.setUsername(email);

        when(dbUserRepository.findByUsername(email)).thenReturn(existingUser);

        DBUser result = dbUserService.signUp(email, password, firstName, lastName);

        assertNull(result);

        verify(dbUserRepository, never()).save(any(DBUser.class));
    }

    @Test
    public void testGetUserInformations() {
        String username = "test1@email.com";
        Long userId = 1L;

        DBUser currentUser = new DBUser();
        currentUser.setId(userId);
        currentUser.setUsername(username);
        List<DBUser> initialConnections = new ArrayList<>();
        currentUser.setConnections(initialConnections);

        DBUser newConnection = new DBUser();
        newConnection.setId(2L);
        newConnection.setUsername("newConnection");

        connectionList.add(newConnection);

        when(dbUserRepository.findByUsername(username)).thenReturn(currentUser);
        when(dbUserRepository.findConnectionsByUserId(userId)).thenReturn(connectionList);
        when(dbUserRepository.save(any(DBUser.class))).thenReturn(currentUser);

        DBUser result = dbUserService.getUserInformations(username);

        assertEquals(currentUser.getId(), result.getId());
        assertEquals(currentUser.getUsername(), result.getUsername());
        assertTrue(result.getConnections().contains(newConnection));
    }

    @Test
    public void testAddConnection() {
        String username = "test@email.com";

        DBUser currentUser = new DBUser();
        currentUser.setUsername(username);
        List<DBUser> userConnections = new ArrayList<>();
        currentUser.setConnections(userConnections);

        when(dbUserRepository.findByUsername("user")).thenReturn(currentUser);
        when(dbUserRepository.findByUsername(username)).thenReturn(null);

        boolean result = dbUserService.addConnection("user", username);

        assertFalse(result);
        assertTrue(currentUser.getConnections().isEmpty());
        verify(dbUserRepository, never()).save(any(DBUser.class));
    }

    @Test
    public void testAddConnectionAlreadyConnected() {
        String username = "existing@email.com";

        DBUser currentUser = new DBUser();
        currentUser.setUsername(username);
        List<DBUser> userConnections = new ArrayList<>();

        DBUser existingConnection = new DBUser();
        existingConnection.setUsername(username);
        userConnections.add(existingConnection);
        currentUser.setConnections(userConnections);

        when(dbUserRepository.findByUsername("user")).thenReturn(currentUser);
        when(dbUserRepository.findByUsername(username)).thenReturn(existingConnection);

        boolean result = dbUserService.addConnection("user", username);

        assertFalse(result);
        assertEquals(1, currentUser.getConnections().size());
        verify(dbUserRepository, never()).save(any(DBUser.class));
    }
}
