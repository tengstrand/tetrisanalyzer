package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator1;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;
import com.github.tetrisanalyzer.piecegenerator.PredictablePieceGenerator;
import com.github.tetrisanalyzer.settings.AtariGameSettings;
import com.github.tetrisanalyzer.settings.GameSettings;
import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class GameTest {

    @Test
    @Ignore
    public void playFivePieces() {
        ColoredBoard board = ColoredBoard.create(10, 15);
        BoardEvaluator boardEvaluator = new TengstrandBoardEvaluator1(board.width, board.height);
        PieceGenerator pieceGenerator = new PredictablePieceGenerator("OLIZT");
        GameState result = new GameState(0, board, pieceGenerator, 5);
        GameSettings settings = new AtariGameSettings(board, true);
        Game game = new Game(result, boardEvaluator, settings);
        game.run();

        assertEquals(1, result.rows);

        assertEquals(Board.create(
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|---------Z|",
                "|--------ZZ|",
                "|OO---TTTZL|",
                "¯¯¯¯¯¯¯¯¯¯¯¯"), game.board);
    }

    @Test
    public void slidePiece() {
        Board board = Board.create(
                "|----------|",
                "|----------|",
                "|----------|",
                "|--------OO|",
                "|S--------Z|",
                "¯¯¯¯¯¯¯¯¯¯¯¯");
        BoardEvaluator boardEvaluator = new TengstrandBoardEvaluator1(board.width, board.height);
        PieceGenerator pieceGenerator = new PredictablePieceGenerator("T");
        GameState result = new GameState(0, board, pieceGenerator, 1);
        GameSettings settings = new AtariGameSettings(board, true);
        Game game = new Game(result, boardEvaluator, settings);
        game.run();

        assertEquals(Board.create(
                "|----------|",
                "|----------|",
                "|----------|",
                "|-------TOO|",
                "|S-----TTTZ|",
                "¯¯¯¯¯¯¯¯¯¯¯¯"), game.board);
    }

    @Test
    public void dropPieceWhenSlidingIsDisabled() {
        ColoredBoard board = ColoredBoard.create(
                "|----------|",
                "|----------|",
                "|----------|",
                "|--------OO|",
                "|S--------Z|",
                "¯¯¯¯¯¯¯¯¯¯¯¯");
        BoardEvaluator boardEvaluator = new TengstrandBoardEvaluator1(board.width, board.height);
        PieceGenerator pieceGenerator = new PredictablePieceGenerator("T");
        GameState result = new GameState(0, board, pieceGenerator, 1);
        GameSettings settings = new AtariGameSettings(board);
        Game game = new Game(result, boardEvaluator, settings);
        game.run();

        assertEquals(ColoredBoard.create(
                "|----------|",
                "|----------|",
                "|----------|",
                "|TTT-----OO|",
                "|ST-------Z|",
                "¯¯¯¯¯¯¯¯¯¯¯¯"), game.coloredBoard);
    }

    // 1 000 000 = 200 sec = 5 000 validPieces/sec (sliding on)
    // 1 000 000 = 63 sec = 15 800 validPieces/sec (sliding off)
}
