package com.xtay2.onearmedbandit.services.credits;

import com.xtay2.onearmedbandit.persistence.transactions.TransactionRecord;
import com.xtay2.onearmedbandit.persistence.transactions.TransactionRecordRepository;
import com.xtay2.onearmedbandit.persistence.transactions.TransactionType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

@Service
public class CreditStoreService {

    private final AtomicInteger BALANCE = new AtomicInteger();

    private final TransactionRecordRepository transactionRecordRepository;

    public CreditStoreService(TransactionRecordRepository transactionRecordRepository) {
        this.transactionRecordRepository = transactionRecordRepository;
        var lastTransaction = transactionRecordRepository.findFirstByOrderByIdDesc();
        if(lastTransaction != null)
            BALANCE.set(lastTransaction.getBalance());
    }

    public int getBalance() {
        return BALANCE.get();
    }

    /// Performs an atomic credit transaction.
    ///
    /// @param func receives the current balance and returns the updated amount (non-negative).
    public int transaction(IntUnaryOperator func, TransactionType type) {
        int oldBalance, newBalance;
        synchronized (BALANCE) {
            oldBalance = BALANCE.get();
            newBalance = func.applyAsInt(oldBalance);
            if (newBalance < 0)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Balance");
            BALANCE.set(newBalance);
        }
        transactionRecordRepository.saveAndFlush(new TransactionRecord(newBalance, newBalance - oldBalance, type));
        return newBalance;
    }

}