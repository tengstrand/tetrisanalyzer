package com.github.tetrisanalyzer.text;

import com.github.tetrisanalyzer.game.Duration;
import com.github.tetrisanalyzer.settings.RaceSettings;
import com.github.tetrisanalyzer.settings.SystemSettings;
import org.junit.Test;

import static com.github.tetrisanalyzer.settings.RaceSettingsTest.RACE_SETTINGS;
import static com.github.tetrisanalyzer.settings.SystemSettingsTest.SYSTEM_SETTINGS;
import static junit.framework.Assert.assertEquals;

public class ParametersTest {

    @Test
    public void parameterValuesAsString() {
        String[] strings = parameters().rows();

        String result = "";
        String separator = "";
        for (String string : strings) {
            result += separator + string;
            separator = "\n";
        }

        String expected =
                "parameter value:               3.33               3.55\n" +
                "----------------  -----------------  -----------------\n" +
                "duration:         1d 3h 52m 10.760s  1d 3h 52m 10.760s\n" +
                "games:                            2             19 488\n" +
                "pieces:                   4 000 000        155 367 231\n" +
                "rows:                     1 000 000              6 466\n" +
                "min rows:                         2                  2\n" +
                "max rows:                    27 478             29 919\n" +
                "pieces/s:                     39.87              1 548\n" +
                "rows/game:                  500 000                  0";

        assertEquals(expected, result);
    }

    private Parameters parameters() {
        SystemSettings systemSettings = SystemSettings.fromString(SYSTEM_SETTINGS);
        RaceSettings race = RaceSettings.fromString(RACE_SETTINGS, systemSettings);

        race.games.get(0).gameState.games = 2;
        race.games.get(0).gameState.totalPieces = 4000000;
        race.games.get(0).gameState.totalRows =1000000;
        race.games.get(0).duration = new Duration(1,2,3,4,5);

        return new Parameters(race.games);
    }
}
