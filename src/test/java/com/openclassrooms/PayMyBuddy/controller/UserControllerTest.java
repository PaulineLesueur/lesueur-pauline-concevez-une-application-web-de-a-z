package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.Account;
import com.openclassrooms.PayMyBuddy.model.DBUser;
import com.openclassrooms.PayMyBuddy.service.AccountService;
import com.openclassrooms.PayMyBuddy.service.DBUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DBUserService dbUserService;

    @MockBean
    private AccountService accountService;


    @Test
    @WithMockUser( roles = "USER")
    public void testLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
                // .andExpect(view().name("login"));
    }

    @Test
    @WithMockUser( roles = "USER")
    public void testCreateAccount() throws Exception {
        mockMvc.perform(get("/createAccount"))
                .andExpect(view().name("createAccount"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testSignUp() throws Exception{
        DBUser mockUser = new DBUser();

        when(dbUserService.signUp("test@email.com", "password", "John", "Doe")).thenReturn(mockUser);

        accountService.createAccount(mockUser);
        verify(accountService, times(1)).createAccount(mockUser);

        mockMvc.perform(post("/createAccount")
                        .with(csrf().asHeader())
                        .param("email", "test@email.com")
                        .param("password", "password")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(redirectedUrl("/home/transfer"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testAlreadySignedUp() throws Exception {
        when(dbUserService.signUp("test@email.com", "password", "John", "Doe")).thenReturn(null);

        mockMvc.perform(post("/createAccount")
                        .with(csrf().asHeader())
                        .param("email", "test@email.com")
                        .param("password", "password")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(redirectedUrl("/createAccount"))
                .andExpect(flash().attribute("errorMessage", "The email provided is already associated with an account..."));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testMoneyPage() throws Exception {
        DBUser mockUser = new DBUser();
        mockUser.setUsername("test@email.com");
        mockUser.setAccount(new Account());

        when(dbUserService.getUserByUsername("user")).thenReturn(mockUser);

        mockMvc.perform(get("/home/money"))
                .andExpect(view().name("money"));
    }

}
