package com.github.tetrisanalyzer.text;

import com.github.tetrisanalyzer.settings.RaceGameSettings;

import java.util.List;

public class RaceText {
    public final List<RaceGameSettings> games;

    public RaceText(List<RaceGameSettings> games) {
        this.games = games;
    }

    public List<String> asText() {
        return null;
    }
}
