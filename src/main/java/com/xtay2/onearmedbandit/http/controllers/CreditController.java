package com.xtay2.onearmedbandit.http.controllers;

import com.xtay2.onearmedbandit.http.requests.CreditTransactionRequest;
import com.xtay2.onearmedbandit.http.responses.BalanceResponse;
import com.xtay2.onearmedbandit.http.responses.CreditClearResponse;
import com.xtay2.onearmedbandit.persistence.transactions.TransactionType;
import com.xtay2.onearmedbandit.services.credits.CreditStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/credits")
@RequiredArgsConstructor
public class CreditController {

    private final CreditStoreService creditStoreService;

    @GetMapping("/balance")
    ResponseEntity<BalanceResponse> balance() {
        return ResponseEntity.ok(new BalanceResponse(creditStoreService.getBalance()));
    }

    @PostMapping("/deposit")
    ResponseEntity<BalanceResponse> deposit(@RequestBody CreditTransactionRequest request) {
        if (request.amount() < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be positive");

        var newBalance = creditStoreService.transaction(balance -> balance + request.amount(), TransactionType.DEPOSIT);

        return ResponseEntity.ok(new BalanceResponse(newBalance));
    }

    @PostMapping("/withdraw")
    ResponseEntity<BalanceResponse> withdraw(@RequestBody CreditTransactionRequest request) {
        if (request.amount() < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be positive");

        var newBalance = creditStoreService.transaction(balance -> {
            if (request.amount() > balance)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be less or equal to the balance (" + balance + " credits)");
            return balance - request.amount();
        }, TransactionType.WITHDRAWAL);

        return ResponseEntity.ok(new BalanceResponse(newBalance));
    }

    @PostMapping("/clear")
    ResponseEntity<CreditClearResponse> clear() {
        var oldBalance = new AtomicInteger();
        creditStoreService.transaction(balance -> {
            oldBalance.set(balance);
            return 0;
        }, TransactionType.WITHDRAWAL);
        return ResponseEntity.ok(new CreditClearResponse(oldBalance.get()));
    }

}