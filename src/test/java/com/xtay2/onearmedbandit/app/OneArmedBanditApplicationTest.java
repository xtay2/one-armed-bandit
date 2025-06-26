package com.xtay2.onearmedbandit.app;

import com.xtay2.onearmedbandit.http.controllers.CreditController;
import com.xtay2.onearmedbandit.http.controllers.GameController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class OneArmedBanditApplicationTest {

    @Autowired
    private GameController gameController;

    @Autowired
    private CreditController creditController;

    @Test
    void contextLoads() {
        assertThat(gameController).isNotNull();
        assertThat(creditController).isNotNull();
    }

}