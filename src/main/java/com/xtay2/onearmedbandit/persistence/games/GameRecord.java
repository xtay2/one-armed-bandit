package com.xtay2.onearmedbandit.persistence.games;

import com.xtay2.onearmedbandit.services.game.GameResult;
import com.xtay2.onearmedbandit.services.game.Reel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.Instant;

@Entity
@Table(name = "games")
@Getter
@NoArgsConstructor
public class GameRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Instant timestamp;

    int stake;

    int reward;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    Reel winningReel;

    public GameRecord(int stake, GameResult gameResult) {
        this.timestamp = Instant.now();
        this.reward = gameResult.reward();
        this.winningReel = gameResult.isWon() ? gameResult.rotation().getFirst() : null;
        this.stake = stake;
    }
}