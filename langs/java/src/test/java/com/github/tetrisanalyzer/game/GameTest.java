package nu.tengstrand.tetrisanalyzer.game;

import nu.tengstrand.tetrisanalyzer.board.Board;
import nu.tengstrand.tetrisanalyzer.boardevaluator.BoardEvaluator;
import nu.tengstrand.tetrisanalyzer.boardevaluator.JTengstrandBoardEvaluator1;
import nu.tengstrand.tetrisanalyzer.piecegenerator.PieceGenerator;
import nu.tengstrand.tetrisanalyzer.piecegenerator.PredictablePieceGenerator;
import nu.tengstrand.tetrisanalyzer.settings.GameSettings;
import nu.tengstrand.tetrisanalyzer.settings.GameSettingsSlidingOn;
import org.junit.Test;

import java.util.Arrays;

public class GameTest {

    @Test
    public void playFivePieces() {
        Board board = new Board(10,15);
        BoardEvaluator boardEvaluator = new JTengstrandBoardEvaluator1(board.getWidth(), board.getHeight());
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
        BoardEvaluator boardEvaluator = new JTengstrandBoardEvaluator1(board.getWidth(), board.getHeight());
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
