package com.github.tetrisanalyzer.text;

import com.github.tetrisanalyzer.settings.RaceSettings;
import com.github.tetrisanalyzer.settings.SystemSettings;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.github.tetrisanalyzer.settings.SystemSettingsTest.SYSTEM_SETTINGS;
import static junit.framework.Assert.assertEquals;

public class RaceTextTest {

    private final String RACE_SETTINGS = "game rules id: Standard\n" +
            "piece generator id: Linear\n" +
            "board evaluator id: Tengstrand 1.2\n" +
            "parameter name: areaWidthFactor2\n" +
            "cell area range: [0,380]\n" +
            "games:\n" +
            " - parameter value: 4.51\n" +
            "   duration: 1d 3h 52m 10.760s\n" +
            "   board: [10,12]\n" +
            "   games: 19308\n" +
            "   pieces: 99965282\n" +
            "   rows: 2666\n" +
            "   min rows: 2\n" +
            "   max rows: 27478\n" +
            "   rows/game: 23100\n" +
            "   cell area: 1.814397333\n" +
            "   pieces/s: 10987\n" +
            "   piece generator state: {seed: 12}\n" +
            "   distribution: [1,2,4,8,20,30,10,5,2,1,1]\n" +
            "\n" +
            " - parameter value: 4.61\n" +
            "   duration: 1d 3h 53m 11.0s\n" +
            "   board: [10,12]\n" +
            "   games: 19488\n" +
            "   pieces: 100007231\n" +
            "   rows: 6466\n" +
            "   min rows: 1\n" +
            "   max rows: 29919\n" +
            "   rows/game: 33333\n" +
            "   cell area: 1.8879667333\n" +
            "   pieces/s: 10123\n" +
            "   piece generator state: {seed: 12}\n" +
            "   distribution: [1,3,5,9,21,31,11,4,1,1,1]\n" +
            "\n" +
            " - parameter value: 4.71\n" +
            "   duration: 1d 3h 54m 12.0s\n" +
            "   board: [10,12]\n" +
            "   games: 22222\n" +
            "   pieces: 100007574\n" +
            "   rows: 10358\n" +
            "   min rows: 5\n" +
            "   max rows: 32115\n" +
            "   rows/game: 12345\n" +
            "   cell area: 2.003491333\n" +
            "   pieces/s: 11363\n" +
            "   piece generator state: {seed: 12}\n" +
            "   distribution: [1,3,5,9,21,31,11,4,1,1,1]";

    @Test
    @Ignore
    public void asText() {
        SystemSettings systemSettings = SystemSettings.fromString(SYSTEM_SETTINGS);
        RaceSettings race = RaceSettings.fromString(RACE_SETTINGS, systemSettings);

        RaceText raceText = new RaceText(race.games);

        List<String> expectedResult = Arrays.asList(
                "board: 10 x 12",
                "piece gen. state: { seed: 12 }",
                "",
                "parameter value:        4.51          4.61          4.71",
                "----------------  ----------   -----------   -----------",
                "duration:          1d 3h 52m     1d 3h 53m     1d 3h 54m",
                "games:                19 308        19 488        22 222",
                "pieces            99 965 282   100 007 231   100 007 574",
                "rows:                  2 666         6 466        10 358",
                "min rows:                  2             1             6",
                "max rows:             27 478        29 919        32 115",
                "",
                "cell area:          1.814397     1.8879667      2.003491",
                "pieces/s:             10 987        11 152        11 363",
                "rows/game             23 100        33 333        12 345");

        assertEquals(expectedResult, raceText.asText());
    }
}
