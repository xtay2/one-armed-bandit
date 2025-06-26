package com.xtay2.onearmedbandit.services.credits;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

@Service
public class CreditStoreService {

    private final AtomicInteger BALANCE = new AtomicInteger();

    public int getBalance() {
        return BALANCE.get();
    }

    /// Performs an atomic credit transaction.
    ///
    /// @param func receives the current balance and returns the updated amount (non-negative).
    public int transaction(IntUnaryOperator func) {
        synchronized (BALANCE) {
            var newBalance = func.applyAsInt(BALANCE.get());
            if (newBalance < 0)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Balance");
            BALANCE.set(newBalance);
            return newBalance;
        }
    }

}