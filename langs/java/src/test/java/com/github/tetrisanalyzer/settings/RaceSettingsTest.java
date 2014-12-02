package com.github.tetrisanalyzer.settings;

import com.esotericsoftware.yamlbeans.YamlException;
import com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator12;
import com.github.tetrisanalyzer.gui.WindowLocation;
import com.github.tetrisanalyzer.piecegenerator.LinearCongrentialPieceGenerator;
import org.junit.Test;

import static com.github.tetrisanalyzer.settings.SystemSettingsTest.SYSTEM_SETTINGS;
import static junit.framework.Assert.assertEquals;

public class RaceSettingsTest {

    public static final String RACE_SETTINGS =
            "board: [10,20]\n" +
            "tetris rules id: Standard\n" +
            "piece generator id: Linear\n" +
            "board evaluator id: Tengstrand 1.2\n" +
            "parameter name: areaWidthFactor2\n" +
            "colors: [ff0000, 00ff00, 000000, ffaa00, 00b2ff, b32dd7, cee126, ff00f6, c8c8c8]\n" +
            "window-location-size: [100,200,1139,600]\n" +
            "zoom-windows:\n" +
            "  key-1:\n" +
            "    window-2: [0.278, 0.054, 0.295, 0.150]\n" +
            "    window-1: [0.243, 0.0, 0.351, 0.31]\n" +
            "    window-3: [0.286, 0.084, 0.287, 0.118]\n" +
            "games:\n" +
            " - parameter value: 3.33\n" +
            "   tetris rules id: Atari\n" +
            "   duration: 1d 3h 52m 10.760s\n" +
            "   board: [\"----------\",\n" +
            "           \"----------\",\n" +
            "           \"----------\",\n" +
            "           \"----------\",\n" +
            "           \"----------\",\n" +
            "           \"----------\",\n" +
            "           \"----------\",\n" +
            "           \"----------\",\n" +
            "           \"----------\",\n" +
            "           \"----------\",\n" +
            "           \"----------\",\n" +
            "           \"----------\"]\n" +
            "   games: 19308\n" +
            "   pieces: 63332\n" +
            "   pieces total: 153065282\n" +
            "   rows: 2666\n" +
            "   rows total: 7777777\n" +
            "   min rows: 2\n" +
            "   max rows: 27478\n" +
            "   rows/game: 23100\n" +
            "   pieces/s: 10987\n" +
            "   piece generator state: {seed: 11, constant2: 4444}\n" +
            "   distribution: [1,2,4,8,20,30,10,5,2,1,1]\n" +
            "\n" +
            " - parameter value: 3.55\n" +
            "   duration: 1d 3h 52m 10.760s\n" +
            "   board: [\"----------\",\n" +
            "           \"----------\",\n" +
            "           \"----------\",\n" +
            "           \"----------\",\n" +
            "           \"----------\",\n" +
            "           \"----------\",\n" +
            "           \"----------\",\n" +
            "           \"----------\",\n" +
            "           \"----------\",\n" +
            "           \"----------\",\n" +
            "           \"----------\",\n" +
            "           \"----------\"]\n" +
            "   games: 19488\n" +
            "   pieces: 54343\n" +
            "   pieces total: 155367231\n" +
            "   rows: 6466\n" +
            "   rows total: 88888888\n" +
            "   min rows: 2\n" +
            "   max rows: 29919\n" +
            "   rows/game: 33333\n" +
            "   cells/pos: 1.4142342\n" +
            "   pieces/s: 10123\n" +
            "   piece generator state: {seed: 12}\n" +
            "   distribution: [1,3,5,9,21,31,11,4,1,1,1]";

    @Test
    public void importSettings() throws YamlException {
        SystemSettings systemSettings = SystemSettings.fromString(SYSTEM_SETTINGS);
        RaceSettings race = RaceSettings.fromString(RACE_SETTINGS, systemSettings);

        assertEquals(2, race.games.size());
        assertEquals(19308, race.games.get(0).gameState.games);
        assertEquals(153065282, race.games.get(0).gameState.totalPieces);
        assertEquals(2, race.games.get(0).gameState.minRows);
        assertEquals(27478, race.games.get(0).gameState.maxRows);

        LinearCongrentialPieceGenerator pieceGenerator = (LinearCongrentialPieceGenerator)race.games.get(0).pieceGenerator;
        assertEquals(11, pieceGenerator.seed);
        assertEquals(1664525, pieceGenerator.constant1);
        assertEquals(4444, pieceGenerator.constant2);

        TengstrandBoardEvaluator12 boardEvaluator = (TengstrandBoardEvaluator12)race.games.get(0).boardEvaluator;
        assertEquals(3.33, boardEvaluator.areaWidthFactor2);
        assertEquals(2.5, boardEvaluator.heightFactor1);

        assertEquals("zoom-windows:\n" +
                "  key-1:\n" +
                "    window-1: [0.243, 0.0, 0.351, 0.31]\n" +
                "    window-2: [0.278, 0.054, 0.295, 0.15]\n" +
                "    window-3: [0.286, 0.084, 0.287, 0.118]\n", race.shortcuts.export());
    }

    @Test
    public void exportSettings() throws YamlException {
        SystemSettings systemSettings = SystemSettings.fromString(SYSTEM_SETTINGS);
        RaceSettings race = RaceSettings.fromString(RACE_SETTINGS, systemSettings);

        for (RaceGameSettings settings : race.games) {
            settings.createGame(settings.tetrisRules);
        }
        String result = race.export(new WindowLocation(), "");

        assertEquals(
                "board: [10,20]\n" +
                "tetris rules id: Standard\n" +
                "piece generator id: Linear\n" +
                "board evaluator id: Tengstrand 1.2\n" +
                "parameter name: areaWidthFactor2\n" +
                "colors: [ff0000, 00ff00, 000000, ffaa00, 00b2ff, b32dd7, cee126, ff00f6, c8c8c8]\n" +
                "window-location-size: [100,200,750,600]\n" +
                "games:\n" +
                " - parameter value: 3.33\n" +
                "   tetris rules id: Atari\n" +
                "   duration: 1d 3h 52m 10.760s\n" +
                "   board: [\"----------\",\n" +
                "           \"----------\",\n" +
                "           \"----------\",\n" +
                "           \"----------\",\n" +
                "           \"----------\",\n" +
                "           \"----------\",\n" +
                "           \"----------\",\n" +
                "           \"----------\",\n" +
                "           \"----------\",\n" +
                "           \"----------\",\n" +
                "           \"----------\",\n" +
                "           \"----------\"]\n" +
                "   games: 19308\n" +
                "   pieces: 63332\n" +
                "   pieces total: 153065282\n" +
                "   rows: 2666\n" +
                "   rows total: 7777777\n" +
                "   min rows: 2\n" +
                "   max rows: 27478\n" +
                "   rows/game: 402\n" +
                "   piece/s: 1525\n" +
                "   piece generator state: { seed: 11, constant2: 4444 }\n" +
                "   distribution: [1,2,4,8,20,30,10,5,2,1,1]\n" +
                " - parameter value: 3.55\n" +
                "   duration: 1d 3h 52m 10.760s\n" +
                "   board: [\"----------\",\n" +
                "           \"----------\",\n" +
                "           \"----------\",\n" +
                "           \"----------\",\n" +
                "           \"----------\",\n" +
                "           \"----------\",\n" +
                "           \"----------\",\n" +
                "           \"----------\",\n" +
                "           \"----------\",\n" +
                "           \"----------\",\n" +
                "           \"----------\",\n" +
                "           \"----------\"]\n" +
                "   games: 19488\n" +
                "   pieces: 54343\n" +
                "   pieces total: 155367231\n" +
                "   rows: 6466\n" +
                "   rows total: 88888888\n" +
                "   min rows: 2\n" +
                "   max rows: 29919\n" +
                "   rows/game: 4561\n" +
                "   piece/s: 1548\n" +
                "   piece generator state: { seed: 12, constant2: 555555555 }\n" +
                "   distribution: [1,3,5,9,21,31,11,4,1,1,1]\n", result);
    }
}
