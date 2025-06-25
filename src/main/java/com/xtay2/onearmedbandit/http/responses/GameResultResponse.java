package com.xtay2.onearmedbandit.http.responses;

import com.xtay2.onearmedbandit.services.game.GameResult;
import com.xtay2.onearmedbandit.services.game.Reel;

import java.util.List;

public record GameResultResponse(int reward, List<Reel> rotation) {

    public GameResultResponse(GameResult result) {
        this(result.reward(), result.rotation());
    }

}