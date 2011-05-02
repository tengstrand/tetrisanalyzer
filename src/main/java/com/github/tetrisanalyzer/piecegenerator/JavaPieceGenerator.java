package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.piece.Piece;

import java.util.Random;

/**
 * Uses the standard random generator in Java.
 */
public class JavaPieceGenerator extends PieceGenerator {
    private Random random = new Random();

    @Override
    public int nextPieceNumber() {
        return random.nextInt(Piece.NUMBER_OF_PIECE_TYPES) + 1;
    }
}
