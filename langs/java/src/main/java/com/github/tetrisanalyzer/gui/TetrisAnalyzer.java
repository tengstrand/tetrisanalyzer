package com.github.tetrisanalyzer.gui;

import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator1;
import com.github.tetrisanalyzer.game.Game;
import com.github.tetrisanalyzer.game.GameMessage;
import com.github.tetrisanalyzer.game.GameState;
import com.github.tetrisanalyzer.piecegenerator.LinearCongrentialPieceGenerator;
import com.github.tetrisanalyzer.settings.GameSettings;
import com.github.tetrisanalyzer.settings.StandardGameSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import static com.github.tetrisanalyzer.game.StringUtils.format;

public class TetrisAnalyzer extends JPanel implements MouseMotionListener {

    private long paintedFrames;
    private Image offscreenImage;
    private final GameMessage message;

    private static Font monospacedFont = new Font("monospaced", Font.PLAIN, 12);

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Tetris Analyzer 3.0 - by Joakim Tengstrand");
        frame.setLayout(new GridLayout());

        Game game1 = newGame("1", 1);
        Game game2 = newGame("2", 2);
        Game game3 = newGame("3", 3);
        Game game4 = newGame("4", 4);

        frame.getContentPane().add(new TetrisAnalyzer(game1.message));
        frame.getContentPane().add(new TetrisAnalyzer(game2.message));
        frame.getContentPane().add(new TetrisAnalyzer(game3.message));
        frame.getContentPane().add(new TetrisAnalyzer(game4.message));
        frame.setSize(900, 620);
        frame.setLocation(300, 300);
        frame.setVisible(true);

        new Thread(game1).start();
        new Thread(game2).start();
        new Thread(game3).start();
        new Thread(game4).start();
    }

    private static Game newGame(String id, long seed) {
        ColoredBoard board = ColoredBoard.create(10, 20);
//        ColoredBoard board = ColoredBoard.create(5, 5);
        //Board board = Board.create(10, 15);
        GameSettings settings = new StandardGameSettings(board, false);
        LinearCongrentialPieceGenerator pieceGenerator = new LinearCongrentialPieceGenerator(seed, settings);
        BoardEvaluator boardEvaluator = new TengstrandBoardEvaluator1(board.width, board.height);

        GameState result = new GameState(id, board, pieceGenerator, 0);
        return new Game(result, boardEvaluator, settings);
    }

    public TetrisAnalyzer(GameMessage message) {
        this.message = message;
        addMouseMotionListener(this);
        setVisible(true);

        setPreferredSize(new Dimension(300, 300));
    }

    public void mouseMoved(MouseEvent me) {
        int x = (int) me.getPoint().getX();
        int y = (int) me.getPoint().getY();
    }

    public void mouseDragged(MouseEvent me) {
        mouseMoved(me);
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        // Clear the offscreen image.
        Dimension d = getSize();
        checkOffscreenImage();
        Graphics offG = offscreenImage.getGraphics();
        offG.setColor(Color.white);
        offG.fillRect(0, 0, d.width, d.height);

        // Draw into the offscreen image.
        paintOffscreen(offscreenImage.getGraphics());
        // Put the offscreen image on the screen.
        g.drawImage(offscreenImage, 0, 0, null);
    }

    private void checkOffscreenImage() {
        Dimension d = getSize();
        if (offscreenImage == null || offscreenImage.getWidth(null) != d.width
                || offscreenImage.getHeight(null) != d.height) {
            offscreenImage = createImage(d.width, d.height);
        }
    }

    public void paintOffscreen(Graphics g) {
        g.setColor(Color.black);
        g.setFont(monospacedFont);

        GameState state = message.getGameState();
        String duration = "Duration: " + state.duration.asString();
        String framesPerSec = "Frames/s: " + state.duration.xPerSeconds(paintedFrames++);
        String pieces = "Pieces: " + format(state.moves);

        String games = "Games: " + format(state.games);
        String rows = "Rows: " + format(state.rows);
        String rowsPerGame = "Rows/game: " + state.rowsPerGame();
        String minRows = "Min rows: " + state.minRows();
        String maxRows = "Max rows: " + state.maxRows();
        String cellsPerPos = "Cells/pos: " + round(state.numberOfCells / (double) state.moves);
        String piecesPerSec = "Pieces/s: " + state.duration.xPerSeconds(state.moves);

        paintTexts(g, 0, framesPerSec, duration, pieces, rows, rowsPerGame, minRows, maxRows, "", games, cellsPerPos, piecesPerSec);
        paintTexts(g, 12, message.board);

        repaint();
        sleep(20);
    }

    double round(double value) {
        return ((int)(value * 10000)) / 10000.0;
    }

    private void paintTexts(Graphics g, int startRow, String... texts) {
        for (int row=0; row < texts.length; row++) {
            paintText(texts[row], startRow + row, g);
        }
    }

    private void paintText(String text, int row, Graphics g) {
        g.drawChars(text.toCharArray(), 0, text.length(), 20, 30 + 16 * row);
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }
}