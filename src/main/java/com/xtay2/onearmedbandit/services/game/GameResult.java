package com.xtay2.onearmedbandit.services.game;

import java.util.List;

public record GameResult(int reward, List<Reel> rotation) {}