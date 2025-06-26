package com.xtay2.onearmedbandit.persistence.games;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRecordRepository extends JpaRepository<GameRecord, Long> {}