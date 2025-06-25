package com.xtay2.onearmedbandit.services.credits;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

public class CreditStore {

    private static final AtomicInteger BALANCE = new AtomicInteger();

    public static int getBalance() {
        return BALANCE.get();
    }

    /// Performs an atomic credit transaction.
    ///
    /// @param func receives the current balance and returns the updated amount (non-negative).
    public static int transaction(IntUnaryOperator func) {
        synchronized (BALANCE) {
            var res = func.applyAsInt(BALANCE.get());
            if (res < 0)
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid Balance");
            BALANCE.set(res);
            return res;
        }
    }

}