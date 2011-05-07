package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.Timer;
import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator1;
import com.github.tetrisanalyzer.piece.*;
import com.github.tetrisanalyzer.piecegenerator.DefaultPieceGenerator;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;
import com.github.tetrisanalyzer.piecegenerator.PredictablePieceGenerator;
import com.github.tetrisanalyzer.settings.DefaultGameSettings;
import com.github.tetrisanalyzer.settings.GameSettings;
import com.github.tetrisanalyzer.settings.GameSettingsSlidingOn;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class GameTest {

    @Test
    public void playFivePieces() {
        Board board = new Board(10,15);
        BoardEvaluator boardEvaluator = new TengstrandBoardEvaluator1(board.getWidth(), board.getHeight());
        PieceGenerator pieceGenerator = new PredictablePieceGenerator(Arrays.asList(new PieceO(), new PieceL(), new PieceI(), new PieceZ(), new PieceT()));
        GameSettings settings = new GameSettingsSlidingOn();
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

/*
    @Test
    public void play() {
        Board board = new Board();
        BoardEvaluator boardEvaluator = new TengstrandBoardEvaluator1(board.getWidth(), board.getHeight());
        PieceGenerator pieceGenerator = new DefaultPieceGenerator();
        GameSettings settings = new DefaultGameSettings(); //GameSettingsSlidingOn();
        Game game = new Game(board, boardEvaluator, pieceGenerator, settings);

        timer1.start();
        game.play(1000000);
        timer1.stop();
        timer1.printResult();
        timer2.printResult();
    }
*/
    // 1 000 000 = 200 sec = 5 000 pieces/sec (sliding on)
    // 1 000 000 = 63 sec = 15 800 pieces/sec (sliding off)
}
