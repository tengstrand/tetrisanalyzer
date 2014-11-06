package com.github.tetrisanalyzer.race;

import com.github.tetrisanalyzer.game.Duration;

public interface Race {
    public Duration duration();
    public int boardWidth();
    public int boardHeight();
}
