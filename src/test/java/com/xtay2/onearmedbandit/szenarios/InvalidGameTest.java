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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/// ### Szenario
/// - Reset the balance to 0
/// - Try to play 1 game with stake 100
/// - Deposit 10 credits
/// - Try to play 1 game with stake 1
/// - Try to play 1 game with stake -50
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InvalidGameTest {

    private static int expectedBalance = 0;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void resetBalance() throws Exception {
        mockMvc.perform(post("/credits/clear")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        assertBalance(expectedBalance);
    }

    @Test
    @Order(2)
    void tryPlayStake100() throws Exception {
        mockMvc.perform(post("/play")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "stake": 100
                                }
                                """))
                .andDo(print())
                .andExpect(status().isBadRequest());
        assertBalance(expectedBalance);
    }

    @Test
    @Order(3)
    void deposit10Credits() throws Exception {
        int addedBalance = 10;
        mockMvc.perform(post("/credits/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": %d
                                }
                                """.formatted(addedBalance)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(expectedBalance + addedBalance));
        assertBalance(expectedBalance + addedBalance);
    }

    @Test
    @Order(4)
    void tryPlayStake1() throws Exception {
        mockMvc.perform(post("/play")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "stake": 1
                                }
                                """))
                .andDo(print())
                .andExpect(status().isPaymentRequired());
        assertBalance(expectedBalance);
    }

    @Test
    @Order(5)
    void tryPlayStakeNegative() throws Exception {
        mockMvc.perform(post("/play")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "stake": -50
                                }
                                """))
                .andDo(print())
                .andExpect(status().isPaymentRequired());
        assertBalance(expectedBalance);
    }

    private void assertBalance(int balance) throws Exception {
        mockMvc.perform(get("/credits/balance"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(balance));
        expectedBalance = balance;
    }

}