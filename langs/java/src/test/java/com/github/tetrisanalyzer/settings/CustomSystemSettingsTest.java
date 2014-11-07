package com.github.tetrisanalyzer.settings;

import org.junit.Test;

public class CustomSystemSettingsTest {

    @Test
    public void test() {
        String settings = "\n" +
                "game rules:\n" +
                " - id: Standard\n" +
                "   description: Standard Tetris 2007 June 4 (Colin Fahey)\n" +
                "   url: http://colinfahey.com/tetris/tetris.html\n" +
                "   sliding: off\n" +
                "   rotation: anticlockwise\n" +
                "   piece start position on standard board: [3,0]\n" +
                "   O: [[1,1]]\n" +
                "   I: [[0,1] [2,0]]\n" +
                "   S: [[1,1] [2,0]]\n" +
                "   Z: [[1,1] [2,0]]\n" +
                "   L: [[1,1] [2,0] [1,0] [1,0]]\n" +
                "   J: [[1,1] [2,0] [1,0] [1,0]]\n" +
                "   T: [[1,1] [2,0] [1,0] [1,0]]\n" +
                "\n" +
                " - id: Atari\n" +
                "   description: Atari arcade game\n" +
                "   url: http://www.arcade-museum.com/game_detail.php?game_id=10081\n" +
                "   sliding: off\n" +
                "   rotation: anticlockwise\n" +
                "   piece start position on standard board: [3,0]\n" +
                "   O: [[0,0]]\n" +
                "   I: [[0,1] [1,0]]\n" +
                "   S: [[0,0] [0,0]]\n" +
                "   Z: [[0,0] [0,0]]\n" +
                "   L: [[0,0] [0,0] [0,0] [0,0]]\n" +
                "   J: [[0,0] [0,0] [0,0] [0,0]]\n" +
                "   T: [[0,0] [0,0] [0,0] [0,0]]\n" +
                "   \n" +
                "piece generators:\n" +
                "  - id: linear\n" +
                "    description: Standard Linear congruential piece generator\n" +
                "    class: com.github.tetrisanalyzer.piecegenerator.LinearCongrentialPieceGenerator\n" +
                "    constant 1: 1664525\n" +
                "    constant 2: 1013904223\n" +
                "    seed: 1" +
                "\n" +
                "board evaluators:\n" +
                "  - id: Tengstrand1\n" +
                "    description: Tengstrand 1.1\n" +
                "    author: Joakim Tengstrand\n" +
                "    url: http://hem.bredband.net/joakimtengstrand\n" +
                "    class: com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator1\n" +
                "    height factor: [7.0, 2.5, 2.2, 1.8, 1.3, 1.0, 0.9, 0.7, 0.6, 0.5, 0.4, 0.3, 0.25, 0.2, 0.15, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1]\n" +
                "    hollow factor: [0.0, 0.0, 0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.553]\n" +
                "    area width factor: [0.0, 4.95, 2.39, 3.1, 2.21, 2.05, 1.87, 1.52, 1.34, 1.18, 0.0]\n" +
                "    area height factor: [0.0, 0.5, 1.19, 2.3, 3.1, 4.6, 5.6, 6.6, 7.6, 8.6, 9.6, 10.6, 11.6, 12.6, 13.6, 14.6, 15.6, 16.6, 17.6, 18.6, 19.6]\n" +
                "    area height factor2: [0.0, 0.42, 1.05, 2.2, 3.1, 4.6, 5.6, 6.6, 7.6, 8.6, 9.6, 10.6, 11.6, 12.6, 13.6, 14.6, 15.6, 16.6, 17.6, 18.6, 19.6]";

        SystemSettings systemSettings = CustomSystemSettings.fromString(settings);

        int xx = 1;

    }
}
