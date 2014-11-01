package com.github.tetrisanalyzer.board;

import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.piece.Piece;

public interface TextBoard {
    String[] asStringRows(Piece piece, Move move);
}
