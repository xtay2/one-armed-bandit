package com.xtay2.onearmedbandit.services.credits;

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
            assert res >= 0 : "Invalid Balance";
            BALANCE.set(res);
            return res;
        }
    }

}