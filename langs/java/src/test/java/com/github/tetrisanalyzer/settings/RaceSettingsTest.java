package com.github.tetrisanalyzer.settings;

import com.esotericsoftware.yamlbeans.YamlException;
import com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator13;
import com.github.tetrisanalyzer.gui.WindowLocation;
import com.github.tetrisanalyzer.piecegenerator.LinearCongrentialPieceGenerator;
import org.junit.Test;

import static com.github.tetrisanalyzer.settings.SystemSettingsTest.SYSTEM_SETTINGS;
import static junit.framework.Assert.assertEquals;

public class RaceSettingsTest {

    public static final String RACE_SETTINGS =
            "start board: [\"----------\",\n" +
            "              \"----------\",\n" +
            "              \"----------\",\n" +
            "              \"----------\",\n" +
            "              \"----------\",\n" +
            "              \"----------\",\n" +
            "              \"----------\",\n" +
            "              \"----------\",\n" +
            "              \"----------\",\n" +
            "              \"----------\",\n" +
            "              \"xx------xx\",\n" +
            "              \"xx----xxxx\"]\n" +
            "tetris rules id: Standard\n" +
            "piece generator id: Linear\n" +
            "board evaluator id: Tengstrand 1.2\n" +
            "parameter name: areaWidthFactor2\n" +
            "parameters:\n" +
            "  hollowFactor2: 0.61\n" +
            "  hollowFactorDelta: 0.86\n" +
            "colors: [ff0000, 00ff00, 000000, ffaa00, 00b2ff, b32dd7, cee126, ff00f6, c8c8c8]\n" +
            "window location size: [100,200,1139,600]\n" +
            "restart-on-file-change: true\n" +
            "zoom windows:\n" +
            "  key-1:\n" +
            "    window-2: [0.278, 0.054, 0.295, 0.150]\n" +
            "    window-1: [0.243, 0.0, 0.351, 0.31]\n" +
            "    window-3: [0.286, 0.084, 0.287, 0.118]\n" +
            "save on close: false\n" +
            "area %: 25\n" +
            "games:\n" +
            " - duration: 1d 0h 0m 0s\n" +
            "   level: 3\n" +
            "   number of known pieces: 2\n" +
            "   next pieces: [L]\n" +
            "   heading: first value\n" +
            "   hide: true\n" +
            " - duration: 1d 3h 52m 10.760s\n" +
            "   heading: second value\n" +
            "   parameters:\n" +
            "     board evaluator id: Tengstrand 1.3\n" +
            "     hollowFactor1: 0.54\n" +
            "     areaWidthFactor2: 2.38\n" +
            "   level: 1\n" +
            "   master depth: 1\n" +
            "   total equity diff: 10.0\n" +
            "   number of known pieces: 1\n" +
            "   next pieces: [T]\n" +
            "   tetris rules id: Atari\n" +
            "   board evaluator id: Tengstrand 1.2\n" +
            "   paused: true\n" +
            "   color: aabbcc\n" +
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
            "           \"SS--------\"]\n" +
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
            " - duration: 1d 3h 52m 10.760s\n" +
            "   parameter value: 3.55\n" +
            "   level: 1\n" +
            "   number of known pieces: 1\n" +
            "   next pieces: [T]\n" +
            "   start board: [\"----------\",\n" +
            "                 \"----------\",\n" +
            "                 \"----------\",\n" +
            "                 \"----------\",\n" +
            "                 \"----------\",\n" +
            "                 \"----------\",\n" +
            "                 \"----------\",\n" +
            "                 \"----------\",\n" +
            "                 \"----------\",\n" +
            "                 \"----------\",\n" +
            "                 \"----------\",\n" +
            "                 \"xx------xx\"]\n" +
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
        RaceSettings race = RaceSettings.fromString(RACE_SETTINGS, systemSettings, false);

        assertEquals(2, race.games.size());
        assertEquals(19308, race.games.get(0).gameState.games);
        assertEquals(153065282, race.games.get(0).gameState.totalPieces);
        assertEquals(2, race.games.get(0).gameState.minRows);
        assertEquals(27478, race.games.get(0).gameState.maxRows);

        LinearCongrentialPieceGenerator pieceGenerator = (LinearCongrentialPieceGenerator)race.games.get(0).pieceGenerator;
        assertEquals(11, pieceGenerator.seed);
        assertEquals(1664525, pieceGenerator.constant1);
        assertEquals(4444, pieceGenerator.constant2);

        TengstrandBoardEvaluator13 boardEvaluator = (TengstrandBoardEvaluator13)race.games.get(0).boardEvaluator;
        assertEquals(2.5, boardEvaluator.heightFactor1);
        assertEquals(0.54, boardEvaluator.hollowFactor1);
        assertEquals(2.38, boardEvaluator.areaWidthFactor2);

        assertEquals("zoom windows:\n" +
                "  key-1:\n" +
                "    window-1: [0.243, 0.0, 0.351, 0.31]\n" +
                "    window-2: [0.278, 0.054, 0.295, 0.15]\n" +
                "    window-3: [0.286, 0.084, 0.287, 0.118]\n", race.shortcuts.export());
    }

    @Test
    public void exportSettings() throws YamlException {
        SystemSettings systemSettings = SystemSettings.fromString(SYSTEM_SETTINGS);
        RaceSettings race = RaceSettings.fromString(RACE_SETTINGS, systemSettings, false);
        String result = race.export(new WindowLocation(), "");

        assertEquals(
                "start board: [\"----------\",\n" +
                "              \"----------\",\n" +
                "              \"----------\",\n" +
                "              \"----------\",\n" +
                "              \"----------\",\n" +
                "              \"----------\",\n" +
                "              \"----------\",\n" +
                "              \"----------\",\n" +
                "              \"----------\",\n" +
                "              \"----------\",\n" +
                "              \"xx------xx\",\n" +
                "              \"xx----xxxx\"]\n" +
                "tetris rules id: Standard\n" +
                "piece generator id: Linear\n" +
                "board evaluator id: Tengstrand 1.2\n" +
                "parameter name: areaWidthFactor2\n" +
                "parameters:\n" +
                "  hollowFactor2: 0.61\n" +
                "  hollowFactorDelta: 0.86\n" +
                "save on close: false\n" +
                "restart on file change: false\n" +
                "colors: [ff0000, 00ff00, 000000, ffaa00, 00b2ff, b32dd7, cee126, ff00f6, c8c8c8]\n" +
                "window location size: [100,200,750,600]\n" +
                "area %: 25.0\n" +
                "games:\n" +
                " - heading: first value\n" +
                "   level: 3\n" +
                "   number of known pieces: 2\n" +
                "   next pieces: [L, L]\n" +
                "   duration: 1d 0h 0m 0.000s\n" +
                "   hide: true\n" +
                "   paused: false\n" +
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
                "           \"xx------xx\",\n" +
                "           \"xx----xxxx\"]\n" +
                "   games: \n" +
                "   pieces: 0\n" +
                "   pieces total: 0\n" +
                "   rows: 0\n" +
                "   rows total: 0\n" +
                "   min rows: \n" +
                "   max rows: \n" +
                "   rows/game: \n" +
                "   piece/s: 0.0\n" +
                "   piece generator state: { seed: 557220080, constant2: 555555555 }\n" +
                "   distribution: [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]\n" +
                "   used parameters:\n" +
                "      id: Tengstrand 1.2\n" +
                "      description: Tengstrand 1.2\n" +
                "      author: Joakim Tengstrand\n" +
                "      url: http://hem.bredband.net/joakimtengstrand\n" +
                "      class: com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator12\n" +
                "      maxEquityFactor: 1.21\n" +
                "      heightFactor0: 7.0\n" +
                "      heightFactor1: 2.5\n" +
                "      heightFactorDelta: 0.86\n" +
                "      hollowFactor1: 0.533\n" +
                "      hollowFactor2: 0.61\n" +
                "      hollowFactorDelta: 0.86\n" +
                "      areaWidthFactor1: 4.95\n" +
                "      areaWidthFactor2: 2.39\n" +
                "      areaWidthFactor3: 3.1\n" +
                "      areaWidthFactor4: 2.21\n" +
                "      areaWidthFactor5: 2.05\n" +
                "      areaWidthFactor6: 1.87\n" +
                "      areaWidthFactor7: 1.52\n" +
                "      areaWidthFactor8: 1.34\n" +
                "      areaWidthFactor9: 1.18\n" +
                "      areaHeightFactor1: 0.5\n" +
                "      areaHeightEqFactor1: 0.42\n" +
                "      areaHeightFactor2: 1.19\n" +
                "      areaHeightEqFactor2: 1.05\n" +
                "      areaHeightFactor3: 2.3\n" +
                "      areaHeightEqFactor3: 2.2\n" +
                "      areaHeightFactor4: 3.1\n" +
                "      areaHeightEqFactor4: 3.06\n" +
                "      areaHeightFactor5: 4.6\n" +
                "      areaHeightFactorDelta: 1.0\n" +
                " - heading: second value\n" +
                "   parameters:\n" +
                "     areaWidthFactor2: 2.38\n" +
                "     board evaluator id: Tengstrand 1.3\n" +
                "     hollowFactor1: 0.54\n" +
                "   level: 1\n" +
                "   master depth: 1\n" +
                "   total equity diff: 10.0\n" +
                "   number of known pieces: 1\n" +
                "   next pieces: [T]\n" +
                "   duration: 1d 3h 52m 10.760s\n" +
                "   hide: false\n" +
                "   paused: true\n" +
                "   tetris rules id: Atari\n" +
                "   board evaluator id: Tengstrand 1.2\n" +
                "   color: aabbcc\n" +
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
                "           \"SS--------\"]\n" +
                "   games: 19308\n" +
                "   pieces: 63332\n" +
                "   pieces total: 153065282\n" +
                "   rows: 2666\n" +
                "   rows total: 7777777\n" +
                "   min rows: 2\n" +
                "   max rows: 27478\n" +
                "   rows/game: 402.82665216490574\n" +
                "   piece/s: 1525.6182796770656\n" +
                "   piece generator state: { seed: 11, constant2: 4444 }\n" +
                "   distribution: [1,2,4,8,20,30,10,5,2,1,1]\n" +
                "   used parameters:\n" +
                "      id: Tengstrand 1.3\n" +
                "      description: Tengstrand 1.3\n" +
                "      author: Joakim Tengstrand\n" +
                "      url: http://hem.bredband.net/joakimtengstrand\n" +
                "      class: com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator13\n" +
                "      maxEquityFactor: 1.21\n" +
                "      heightFactor0: 7.0\n" +
                "      heightFactor1: 2.5\n" +
                "      heightFactorDelta: 0.86\n" +
                "      hollowFactor1: 0.54\n" +
                "      hollowFactor2: 0.61\n" +
                "      hollowFactor3: 0.5\n" +
                "      hollowFactor4: 0.4\n" +
                "      hollowFactor5: 0.34400000000000003\n" +
                "      hollowFactor6: 0.29584\n" +
                "      hollowFactor7: 0.2544224\n" +
                "      hollowFactor8: 0.218803264\n" +
                "      hollowFactor9: 0.18817080704\n" +
                "      hollowFactorDelta: 0.86\n" +
                "      areaWidthFactor1: 4.95\n" +
                "      areaWidthFactor2: 2.38\n" +
                "      areaWidthFactor3: 3.1\n" +
                "      areaWidthFactor4: 2.21\n" +
                "      areaWidthFactor5: 2.05\n" +
                "      areaWidthFactor6: 1.87\n" +
                "      areaWidthFactor7: 1.52\n" +
                "      areaWidthFactor8: 1.34\n" +
                "      areaWidthFactor9: 1.18\n" +
                "      areaHeightFactor1: 0.5\n" +
                "      areaHeightEqFactor1: 0.42\n" +
                "      areaHeightFactor2: 1.19\n" +
                "      areaHeightEqFactor2: 1.05\n" +
                "      areaHeightFactor3: 2.3\n" +
                "      areaHeightEqFactor3: 2.2\n" +
                "      areaHeightFactor4: 3.1\n" +
                "      areaHeightEqFactor4: 3.06\n" +
                "      areaHeightFactor5: 4.6\n" +
                "      areaHeightFactorDelta: 1.0\n" +
                " - parameter value: 3.55\n" +
                "   level: 1\n" +
                "   number of known pieces: 1\n" +
                "   next pieces: [T]\n" +
                "   duration: 1d 3h 52m 10.760s\n" +
                "   hide: false\n" +
                "   paused: false\n" +
                "   start board: [\"----------\",\n" +
                "                 \"----------\",\n" +
                "                 \"----------\",\n" +
                "                 \"----------\",\n" +
                "                 \"----------\",\n" +
                "                 \"----------\",\n" +
                "                 \"----------\",\n" +
                "                 \"----------\",\n" +
                "                 \"----------\",\n" +
                "                 \"----------\",\n" +
                "                 \"----------\",\n" +
                "                 \"xx------xx\"]\n" +
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
                "   rows/game: 4561.211412151068\n" +
                "   piece/s: 1548.5620552177813\n" +
                "   piece generator state: { seed: 12, constant2: 555555555 }\n" +
                "   distribution: [1,3,5,9,21,31,11,4,1,1,1]\n" +
                "   used parameters:\n" +
                "      id: Tengstrand 1.2\n" +
                "      description: Tengstrand 1.2\n" +
                "      author: Joakim Tengstrand\n" +
                "      url: http://hem.bredband.net/joakimtengstrand\n" +
                "      class: com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator12\n" +
                "      maxEquityFactor: 1.21\n" +
                "      heightFactor0: 7.0\n" +
                "      heightFactor1: 2.5\n" +
                "      heightFactorDelta: 0.86\n" +
                "      hollowFactor1: 0.533\n" +
                "      hollowFactor2: 0.61\n" +
                "      hollowFactorDelta: 0.86\n" +
                "      areaWidthFactor1: 4.95\n" +
                "      areaWidthFactor2: 3.55\n" +
                "      areaWidthFactor3: 3.1\n" +
                "      areaWidthFactor4: 2.21\n" +
                "      areaWidthFactor5: 2.05\n" +
                "      areaWidthFactor6: 1.87\n" +
                "      areaWidthFactor7: 1.52\n" +
                "      areaWidthFactor8: 1.34\n" +
                "      areaWidthFactor9: 1.18\n" +
                "      areaHeightFactor1: 0.5\n" +
                "      areaHeightEqFactor1: 0.42\n" +
                "      areaHeightFactor2: 1.19\n" +
                "      areaHeightEqFactor2: 1.05\n" +
                "      areaHeightFactor3: 2.3\n" +
                "      areaHeightEqFactor3: 2.2\n" +
                "      areaHeightFactor4: 3.1\n" +
                "      areaHeightEqFactor4: 3.06\n" +
                "      areaHeightFactor5: 4.6\n" +
                "      areaHeightFactorDelta: 1.0\n", result);
    }
}
