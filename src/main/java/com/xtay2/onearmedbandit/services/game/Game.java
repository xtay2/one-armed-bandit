package com.xtay2.onearmedbandit.services.game;

import com.xtay2.onearmedbandit.services.credits.CreditStore;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class Game {

    /// The minimum stake the player has to bet.
    public static final int MIN_STAKE = 3;

    /// The amount of reels, this bandit has.
    public static final int REEL_AMOUNT = 3;

    public static GameResult play(int stake) {
        var result = simulateNewGame(stake);
        CreditStore.transaction(balance -> {
            if (stake > balance)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stake cannot be greater than balance (" + balance + " credits)");
            balance -= stake;
            balance += result.reward();
            return balance;
        });
        return result;
    }

    /// @return a new random game result.
    private static GameResult simulateNewGame(int stake) {
        assert stake >= MIN_STAKE;
        var rotation = Reel.randomRotation(REEL_AMOUNT);
        var reward = calculateReward(rotation, stake);
        return new GameResult(reward, rotation);
    }

    /// @return the amount of credits, won by the given rotation and stake.
    private static int calculateReward(List<Reel> rotation, int stake) {
        assert stake >= MIN_STAKE;
        for (var reel : Reel.values())
            if (rotation.stream().allMatch(reel::equals))
                return reel.winCredits + (stake - MIN_STAKE) * reel.winCredits;
        return 0;
    }

}