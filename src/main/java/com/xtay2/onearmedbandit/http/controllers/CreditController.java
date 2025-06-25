package com.xtay2.onearmedbandit.http.controllers;

import com.xtay2.onearmedbandit.http.requests.CreditTransactionRequest;
import com.xtay2.onearmedbandit.http.responses.BalanceResponse;
import com.xtay2.onearmedbandit.services.credits.CreditStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CreditController {

    @GetMapping("/credits/balance")
    ResponseEntity<BalanceResponse> balance() {
        return ResponseEntity.ok(new BalanceResponse(CreditStore.getBalance()));
    }

    @PostMapping("/credits/deposit")
    ResponseEntity<BalanceResponse> deposit(@RequestBody CreditTransactionRequest request) {
        if (request.amount() < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be positive");

        var newBalance = CreditStore.transaction(balance -> balance + request.amount());

        return ResponseEntity.ok(new BalanceResponse(newBalance));
    }

    @PostMapping("/credits/withdraw")
    ResponseEntity<BalanceResponse> withdraw(@RequestBody CreditTransactionRequest request) {
        if (request.amount() < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be positive");

        var newBalance = CreditStore.transaction(balance -> {
            if (request.amount() > balance)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be less or equal to the balance (" + balance + " credits)");
            return balance - request.amount();
        });

        return ResponseEntity.ok(new BalanceResponse(newBalance));
    }

}