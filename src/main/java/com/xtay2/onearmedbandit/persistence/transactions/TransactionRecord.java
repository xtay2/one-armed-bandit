package com.xtay2.onearmedbandit.persistence.transactions;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.Instant;

@Entity
@Table(name = "transactions")
@Getter
@NoArgsConstructor
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Instant timestamp;

    int balance;

    int diff;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    TransactionType transactionType;

    public TransactionRecord(int balance, int diff, TransactionType transactionType) {
        this.timestamp = Instant.now();
        this.balance = balance;
        this.diff = diff;
        this.transactionType = transactionType;
    }
}