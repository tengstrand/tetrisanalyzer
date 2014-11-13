package com.github.tetrisanalyzer.game;

import java.util.List;

public class Distribution {
    public int[] squares;

    public Distribution(int boardWidth, int boardHeight) {
        squares = new int[((boardWidth-1) * boardHeight) / 2 + 1];
    }

    public Distribution(List<Integer> squares) {
        this.squares = new int[squares.size()];

        int i = 0;
        for (Integer square : squares) {
            if (i == squares.size()) {
                break;
            }
            this.squares[i++] = square;
        }
    }
}
