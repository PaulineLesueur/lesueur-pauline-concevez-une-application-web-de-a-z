package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.Account;
import com.openclassrooms.PayMyBuddy.model.DBUser;
import com.openclassrooms.PayMyBuddy.service.DBUserService;
import com.openclassrooms.PayMyBuddy.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ConnectionController.class)
public class ConnectionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetails userDetails;

    @MockBean
    private DBUserService dbUserService;

    @MockBean
    private TransactionService transactionService;

    public static List<DBUser> connectionList = new ArrayList<>();

    static {
        connectionList.add(new DBUser(2L, "test2@email.com", "password", "firstname2", "lastname2", "USER", null, null));
        connectionList.add(new DBUser(3L, "test3@email.com", "password", "firstname3", "lastname3", "USER", null, null));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetConnectionAndTransactions() throws Exception {
        DBUser mockUser = new DBUser();
        mockUser.setUsername("test@email.com");
        mockUser.setAccount(new Account());
        mockUser.setConnections(connectionList);

        when(dbUserService.getUserInformations("user")).thenReturn(mockUser);

        mockMvc.perform(get("/home/transfer"))
                .andExpect(view().name("transfer"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testAddConnectionPage() throws Exception {
        DBUser mockUser = new DBUser();
        mockUser.setUsername("test@email.com");
        mockUser.setAccount(new Account());

        when(dbUserService.getUserInformations("user")).thenReturn(mockUser);

        mockMvc.perform(get("/home/transfer/addConnection"))
                .andExpect(view().name("addConnection"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testAddExistingConnection() throws Exception {
        when(dbUserService.addConnection("user", "test@email.com")).thenReturn(true);

        mockMvc.perform(post("/addConnection")
                        .with(csrf().asHeader())
                        .param("email", "test@email.com"))
                .andExpect(redirectedUrl("/home/transfer"))
                .andExpect(flash().attribute("successMessage", "Connection added successfully !"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testAddNotFoundUser() throws Exception {
        when(dbUserService.addConnection("user", "test@email.com")).thenReturn(false);

        mockMvc.perform(post("/addConnection")
                        .with(csrf().asHeader())
                        .param("email", "test@email.com"))
                .andExpect(redirectedUrl("/home/transfer/addConnection"))
                .andExpect(flash().attribute("errorMessage", "User not found or already connected"));
    }
}
