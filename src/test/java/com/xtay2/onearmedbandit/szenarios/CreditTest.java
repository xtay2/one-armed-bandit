package com.xtay2.onearmedbandit.szenarios;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/// ### Szenario
/// - Reset the balance to 0
/// - Try to withdraw 10 credits (overbook)
/// - Deposit 100 credits
/// - Withdraw 10 credits
/// - Try to deposit -10 credits
/// - Try to withdraw -10 credits
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreditTest {

    /// @implNote Stateful between tests.
    private static int expectedBalance = 0;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void setBalance0() throws Exception {
        clearBalance();
    }

    @Test
    @Order(2)
    void tryWithdraw10() throws Exception {
        var withdrawnBalance = 10;
        mockMvc.perform(post("/credits/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": %d
                                }
                                """.formatted(withdrawnBalance)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        assertBalance(expectedBalance);
    }

    @Test
    @Order(3)
    void deposit100() throws Exception {
        var addedBalance = 100;
        mockMvc.perform(post("/credits/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": %d
                                }
                                """.formatted(addedBalance)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(expectedBalance + addedBalance));
        assertBalance(expectedBalance + addedBalance);
    }

    @Test
    @Order(4)
    void withdraw10() throws Exception {
        var withdrawnBalance = 10;
        mockMvc.perform(post("/credits/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": %d
                                }
                                """.formatted(withdrawnBalance)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(expectedBalance - withdrawnBalance));
        assertBalance(expectedBalance - withdrawnBalance);
    }

    @Test
    @Order(5)
    void tryDepositNegative() throws Exception {
        var addedBalance = -10;
        mockMvc.perform(post("/credits/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": %d
                                }
                                """.formatted(addedBalance)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        assertBalance(expectedBalance);
    }

    @Test
    @Order(6)
    void tryWithdrawNegative() throws Exception {
        var withdrawnBalance = -10;
        mockMvc.perform(post("/credits/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": %d
                                }
                                """.formatted(withdrawnBalance)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        assertBalance(expectedBalance);
    }

    void clearBalance() throws Exception {
        mockMvc.perform(post("/credits/clear")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        assertBalance(expectedBalance);
    }

    void assertBalance(int balance) throws Exception {
        mockMvc.perform(get("/credits/balance"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(balance));
        expectedBalance = balance;
    }
}