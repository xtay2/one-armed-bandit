package com.xtay2.onearmedbandit.http.controllers;

import com.xtay2.onearmedbandit.http.requests.GamePlayRequest;
import com.xtay2.onearmedbandit.http.responses.GameResultResponse;
import com.xtay2.onearmedbandit.services.game.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static com.xtay2.onearmedbandit.services.game.GameService.MIN_STAKE;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/play")
    ResponseEntity<GameResultResponse> play(@RequestBody GamePlayRequest request) {
        var stake = request.stake();
        if (stake < 0)
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Stake cannot be negative");
        if (stake < MIN_STAKE)
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Stake cannot be less than minimum (" + MIN_STAKE + " credits)");
        var result = gameService.play(stake);
        return ResponseEntity.ok(new GameResultResponse(result));
    }

}