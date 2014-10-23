package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator1;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;
import com.github.tetrisanalyzer.piecegenerator.PredictablePieceGenerator;
import com.github.tetrisanalyzer.settings.GameSettings;
import com.github.tetrisanalyzer.settings.TetrisAnalyzerGameSettings;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class GameTest {

    @Test
    public void playFivePieces() {
        ColoredBoard board = ColoredBoard.create(10, 15);
        GameSettings settings = new TetrisAnalyzerGameSettings(board, true);
        BoardEvaluator boardEvaluator = new TengstrandBoardEvaluator1(board.width, board.height);
        PieceGenerator pieceGenerator = new PredictablePieceGenerator(settings, "OLIZT");
        GameResult result = new GameResult(board, pieceGenerator, 5);
        Game game = new Game(result, boardEvaluator, settings);
        game.play();

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
        GameSettings settings = new TetrisAnalyzerGameSettings(board, true);
        BoardEvaluator boardEvaluator = new TengstrandBoardEvaluator1(board.width, board.height);
        PieceGenerator pieceGenerator = new PredictablePieceGenerator(settings, "T");
        GameResult result = new GameResult(board, pieceGenerator, 1);
        Game game = new Game(result, boardEvaluator, settings);
        game.play();

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
        GameSettings settings = new TetrisAnalyzerGameSettings(board);
        BoardEvaluator boardEvaluator = new TengstrandBoardEvaluator1(board.width, board.height);
        PieceGenerator pieceGenerator = new PredictablePieceGenerator(settings, "T");
        GameResult result = new GameResult(board, pieceGenerator, 1);
        Game game = new Game(result, boardEvaluator, settings);
        game.play();

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
