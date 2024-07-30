package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.service.MoneyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;


@WebMvcTest(controllers = MoneyController.class)
public class MoneyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetails userDetails;

    @MockBean
    private MoneyService moneyService;

    @Test
    @WithMockUser(roles = "USER")
    public void testDeposit() throws Exception {
        moneyService.deposit("user", 10D);
        verify(moneyService, times(1)).deposit("user", 10D);

        mockMvc.perform(post("/deposit")
                        .with(csrf().asHeader())
                        .param("amount", String.valueOf(10D)))
                .andExpect(redirectedUrl("/home/money"))
                .andExpect(flash().attribute("successMessage", "Deposit performed successfully !"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testWithdrawalSuccess() throws Exception {
        when(moneyService.withdrawal("user", 10D)).thenReturn(true);

        mockMvc.perform(post("/withdrawal")
                        .with(csrf().asHeader())
                        .param("amount", String.valueOf(10D)))
                .andExpect(redirectedUrl("/home/money"))
                .andExpect(flash().attribute("successMessage", "Withdrawal performed successfully !"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testWithdrawalInsufficientFunds() throws Exception {
        when(moneyService.withdrawal("user", 10D)).thenReturn(false);

        mockMvc.perform(post("/withdrawal")
                        .with(csrf().asHeader())
                        .param("amount", String.valueOf(10D)))
                .andExpect(redirectedUrl("/home/money"))
                .andExpect(flash().attribute("errorMessage", "Payment failed : Insufficient account balance."));
    }

}
