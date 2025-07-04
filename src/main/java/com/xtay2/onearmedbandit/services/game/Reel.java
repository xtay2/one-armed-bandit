package com.xtay2.onearmedbandit.services.game;

import java.security.SecureRandom;
import java.util.List;

public enum Reel {

    APPLE(10), BANANA(15), CLEMENTINE(20);

    /// The amount of credits received when getting 3 identical reels of this type.
    public final int winCredits;

    Reel(int winCredits) {
        this.winCredits = winCredits;
    }

    /// @return a random, immutable list with the specified amount of reels.
    public static List<Reel> randomRotation(int reelAmount) {
        var reelValues = Reel.values();
        return new SecureRandom()
                .ints(reelAmount, 0, reelValues.length)
                .mapToObj(randInt -> reelValues[randInt])
                .toList();
    }
}