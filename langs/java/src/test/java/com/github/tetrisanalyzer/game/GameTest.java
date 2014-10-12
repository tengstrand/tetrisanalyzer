package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator1;
import com.github.tetrisanalyzer.piece.PieceI;
import com.github.tetrisanalyzer.piece.PieceL;
import com.github.tetrisanalyzer.piece.PieceO;
import com.github.tetrisanalyzer.piece.PieceT;
import com.github.tetrisanalyzer.piece.PieceZ;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;
import com.github.tetrisanalyzer.piecegenerator.PredictablePieceGenerator;
import com.github.tetrisanalyzer.settings.TetrisAnalyzerGameSettings;
import com.github.tetrisanalyzer.settings.GameSettings;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class GameTest {

    @Test
    public void playFivePieces() {
        Board board = new Board(10,15);
        GameSettings settings = new TetrisAnalyzerGameSettings(true);
        BoardEvaluator boardEvaluator = new TengstrandBoardEvaluator1(board.width, board.height);
        PieceGenerator pieceGenerator = new PredictablePieceGenerator(settings, new PieceO(settings), new PieceL(settings), new PieceI(settings), new PieceZ(settings), new PieceT(settings));
        Game game = new Game(board, boardEvaluator, pieceGenerator, settings);
        game.play(5);

        assertEquals(1, game.getClearedLines());

        assertEquals(Board.create(
                "#----------#",
                "#----------#",
                "#----------#",
                "#----------#",
                "#----------#",
                "#----------#",
                "#----------#",
                "#----------#",
                "#----------#",
                "#----------#",
                "#----------#",
                "#----------#",
                "#---------Z#",
                "#--------ZZ#",
                "#OO---TTTZL#",
                "############"), board);
    }

    // 1 000 000 = 200 sec = 5 000 pieces/sec (sliding on)
    // 1 000 000 = 63 sec = 15 800 pieces/sec (sliding off)
}
