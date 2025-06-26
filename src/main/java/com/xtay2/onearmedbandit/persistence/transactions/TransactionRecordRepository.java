package com.xtay2.onearmedbandit.persistence.transactions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Long> {

    TransactionRecord findFirstByOrderByIdDesc();

}