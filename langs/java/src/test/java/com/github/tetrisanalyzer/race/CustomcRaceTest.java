package com.github.tetrisanalyzer.race;

import com.esotericsoftware.yamlbeans.YamlException;
import com.github.tetrisanalyzer.game.Duration;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class CustomcRaceTest {

    @Test
    public void test() throws YamlException {
        String settings = "duration: 1d 3h 52m 10.760s\n" +
                "board: [10,12]\n" +
                "game rules id: standard\n" +
                "piece generator id: linear\n" +
                "board evaluator id: Tengstrand1\n" +
                "parameter name: area width factor\n" +
                "cell area range: [0,380]\n" +
                "games:\n" +
                " - parameter value: 4.5\n" +
                "   games: 19308\n" +
                "   pieces: 153065282\n" +
                "   rows: 2666\n" +
                "   min rows: 2\n" +
                "   max rows: 27478\n" +
                "   rows/game: 23100\n" +
                "   cell area: 1.2342342\n" +
                "   pieces/s: 10987\n" +
                "   piece generator settings: {seed: 2345342142}\n" +
                "   distribution: [1,2,4,8,20,30,10,5,2,1,1]\n" +
                "\n" +
                " - parameter value: 4.6\n" +
                "   games: 19488\n" +
                "   pieces: 155367231\n" +
                "   rows: 6466\n" +
                "   min rows: 2\n" +
                "   max rows: 29919\n" +
                "   rows/game: 33333\n" +
                "   cells/pos: 1.4142342\n" +
                "   pieces/s: 10123\n" +
                "   piece generator settings: {seed: 3242353412342}\n" +
                "   distribution: [1,3,5,9,21,31,11,4,1,1,1]";

        CustomcRace race = CustomcRace.fromString(settings);

        assertEquals(new Duration(1, 3, 52, 10, 760), race.duration());
    }
}
