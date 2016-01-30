package com.github.tetrisanalyzer.settings;

import org.junit.Test;

import static com.github.tetrisanalyzer.settings.SystemSettingsTest.SYSTEM_SETTINGS;
import static org.junit.Assert.*;

public class RaceGamesSettingsTest {

    @Test
    public void size() throws Exception {
        SystemSettings systemSettings = SystemSettings.fromString(SYSTEM_SETTINGS);
        RaceSettings race = RaceSettings.fromString(RaceSettingsTest.RACE_SETTINGS, systemSettings, false);

        RaceGamesSettings games = race.games;

        assertEquals(2, games.size());
    }
}