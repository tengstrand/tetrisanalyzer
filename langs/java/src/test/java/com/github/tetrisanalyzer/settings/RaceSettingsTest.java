package com.github.tetrisanalyzer.settings;

import com.esotericsoftware.yamlbeans.YamlException;
import com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator1;
import com.github.tetrisanalyzer.piecegenerator.LinearCongrentialPieceGenerator;
import org.junit.Test;

import static com.github.tetrisanalyzer.settings.SystemSettingsTest.SYSTEM_SETTINGS;
import static junit.framework.Assert.assertEquals;

public class RaceSettingsTest {

    @Test
    public void readRaceSettings() throws YamlException {
        String settings = "game rules id: Standard\n" +
                "piece generator id: Linear\n" +
                "board evaluator id: Tengstrand 1.2\n" +
                "parameter name: areaWidthFactor2\n" +
                "cell area range: [0,380]\n" +
                "games:\n" +
                " - parameter value: 3.33\n" +
                "   duration: 1d 3h 52m 10.760s\n" +
                "   board: [10,12]\n" +
                "   games: 19308\n" +
                "   pieces: 153065282\n" +
                "   rows: 2666\n" +
                "   min rows: 2\n" +
                "   max rows: 27478\n" +
                "   rows/game: 23100\n" +
                "   cell area: 1.2342342\n" +
                "   pieces/s: 10987\n" +
                "   piece generator settings: {seed: 11, constant2: 4444}\n" +
                "   distribution: [1,2,4,8,20,30,10,5,2,1,1]\n" +
                "\n" +
                " - parameter value: 3.55\n" +
                "   duration: 1d 3h 52m 10.760s\n" +
                "   board: [10,12]\n" +
                "   games: 19488\n" +
                "   pieces: 155367231\n" +
                "   rows: 6466\n" +
                "   min rows: 2\n" +
                "   max rows: 29919\n" +
                "   rows/game: 33333\n" +
                "   cells/pos: 1.4142342\n" +
                "   pieces/s: 10123\n" +
                "   piece generator settings: {seed: 12}\n" +
                "   distribution: [1,3,5,9,21,31,11,4,1,1,1]";

        SystemSettings systemSettings = SystemSettings.fromString(SYSTEM_SETTINGS);

        RaceSettings race = RaceSettings.fromString(settings, systemSettings);

        assertEquals(2, race.games.size());
        assertEquals(19308, race.games.get(0).numberOfGames);
        assertEquals(153065282, race.games.get(0).numberOfPieces);
        assertEquals(2, race.games.get(0).minRows);
        assertEquals(27478, race.games.get(0).maxRows);

        LinearCongrentialPieceGenerator pieceGenerator = (LinearCongrentialPieceGenerator)race.games.get(0).pieceGenerator;
        assertEquals(11, pieceGenerator.seed);
        assertEquals(1234567, pieceGenerator.constant1);
        assertEquals(4444, pieceGenerator.constant2);

        TengstrandBoardEvaluator1 boardEvaluator = (TengstrandBoardEvaluator1)race.games.get(0).boardEvaluator;
        assertEquals(3.33, boardEvaluator.areaWidthFactor2);
        assertEquals(2.5, boardEvaluator.heightFactor1);
    }
}
