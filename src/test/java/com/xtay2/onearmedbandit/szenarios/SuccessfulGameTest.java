package com.xtay2.onearmedbandit.szenarios;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xtay2.onearmedbandit.http.responses.GameResultResponse;
import com.xtay2.onearmedbandit.services.game.Reel;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/// ### Szenario
/// - Reset the balance to 0
/// - Deposit 10.000 credits
/// - Play 100 games with stake 3 (Costs: 300 credits)
/// - Play 100 games with stake 5 (Costs: 500 credits)
///
/// At the end, the balance should be greater or equal to 800.
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SuccessfulGameTest {

    private static final int MIN_STAKE = 3, REEL_AMOUNT = 3;

    /// @implNote Stateful between tests.
    private static int expectedBalance = 0;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void setBalance0() throws Exception {
        mockMvc.perform(post("/credits/clear")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        assertBalance(expectedBalance);
    }

    @Test
    @Order(2)
    void setBalance10_000() throws Exception {
        var addedBalance = 10_000;
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
    @Order(3)
    void play100GamesWithStake3() throws Exception {
        playNGamesWithStakeX(100, 3);
    }

    @Test
    @Order(4)
    void play100GamesWithStake5() throws Exception {
        playNGamesWithStakeX(100, 5);
    }

    @Test
    @Order(5)
    void assertBalanceGreaterEqual800() {
        assertThat(expectedBalance).isGreaterThanOrEqualTo(800);
    }

    void assertBalance(int balance) throws Exception {
        mockMvc.perform(get("/credits/balance"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(balance));
        expectedBalance = balance;
    }


    @SuppressWarnings("SameParameterValue")
    private void playNGamesWithStakeX(int games, int stake) throws Exception {
        var objectMapper = new ObjectMapper();
        for (int i = 0; i < games; i++) {
            var response = mockMvc.perform(post("/play")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "stake": %d
                                    }
                                    """.formatted(stake)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse();
            var responseBodyJson = objectMapper.readValue(response.getContentAsString(), GameResultResponse.class);
            assertThat(responseBodyJson).isNotNull();
            testGameResult(responseBodyJson, stake);
        }
    }

    private void testGameResult(GameResultResponse response, int stake) throws Exception {
        var rotation = response.rotation();
        var reward = response.reward();
        assertThat(rotation).isNotNull();
        assertThat(rotation.size()).isEqualTo(REEL_AMOUNT);
        for (int i = 0; i < REEL_AMOUNT; i++)
            assertThat(rotation.get(i)).isNotNull();
        for (var reel : Reel.values()) {
            if (rotation.stream().allMatch(reel::equals)) {
                assertThat(reward).isEqualTo(reel.winCredits + (stake - MIN_STAKE) * reel.winCredits);
                assertBalance(expectedBalance - stake + reward);
                return;
            }
        }
        assertBalance(expectedBalance - stake);
    }
}