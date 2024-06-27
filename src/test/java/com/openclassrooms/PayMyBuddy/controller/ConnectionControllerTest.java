package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.DBUser;
import com.openclassrooms.PayMyBuddy.service.DBUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ConnectionController.class)
public class ConnectionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetails userDetails;

    @MockBean
    private DBUserService dbUserService;

    public static List<DBUser> connectionList = new ArrayList<>();

    static {
        connectionList.add(new DBUser(2L, "test2@email.com", "password", "firstname2", "lastname2", "USER", null));
        connectionList.add(new DBUser(3L, "test3@email.com", "password", "firstname3", "lastname3", "USER", null));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetConnectionList() throws Exception {
        DBUser mockUser = new DBUser();
        mockUser.setUsername("test@email.com");
        mockUser.setConnections(connectionList);
        when(userDetails.getUsername()).thenReturn(mockUser.getUsername());

        mockMvc.perform(get("/home/transfer"))
                .andExpect(status().isOk());
    }
}
