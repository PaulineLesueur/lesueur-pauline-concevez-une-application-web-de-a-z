package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.repository.TransactionRepository;
import com.openclassrooms.PayMyBuddy.service.AccountService;
import com.openclassrooms.PayMyBuddy.service.DBUserService;
import com.openclassrooms.PayMyBuddy.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@WebMvcTest(controllers = TransactionController.class)
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetails userDetails;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private DBUserService dbUserService;

    @MockBean
    private AccountService accountService;

    @MockBean
    private TransactionRepository transactionRepository;

    @Test
    @WithMockUser(roles = "USER")
    public void testTransferSuccess() throws Exception {
        when(transactionService.transfer("user", 1L, "description", 10D)).thenReturn(true);

        mockMvc.perform(post("/transfer")
                        .with(csrf().asHeader())
                        .param("receiver", String.valueOf(1L))
                        .param("description", "description")
                        .param("amount", String.valueOf(10D)))
                .andExpect(redirectedUrl("/home/transfer"))
                .andExpect(flash().attribute("successMessage", "Transfer performed successfully ! Please note that a 0.5% fee has been deducted."));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testTransferFail() throws Exception {
        when(transactionService.transfer("user", 1L, "description", 10D)).thenReturn(false);

        mockMvc.perform(post("/transfer")
                        .with(csrf().asHeader())
                        .param("receiver", String.valueOf(1L))
                        .param("description", "description")
                        .param("amount", String.valueOf(10D)))
                .andExpect(redirectedUrl("/home/transfer"))
                .andExpect(flash().attribute("errorMessage", "Transfer failed: Insufficient funds or invalid receiver."));
    }

}
