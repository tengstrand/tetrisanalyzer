package com.github.tetrisanalyzer.gui;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator1;
import com.github.tetrisanalyzer.game.Game;
import com.github.tetrisanalyzer.game.GameMessenger;
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
    private final GameMessenger messenger;

    private static Font monospacedFont = new Font("monospaced", Font.PLAIN, 12);

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Tetris Analyzer 3.0 - by Joakim Tengstrand");

        Game game = newGame();
        new Thread(game).start();

        frame.getContentPane().add(new TetrisAnalyzer(game.message));
        frame.setSize(400, 300);
        frame.setLocation(300, 300);
        frame.setVisible(true);
    }

    private static Game newGame() {
        Board board = Board.create(10, 20);
        GameSettings settings = new StandardGameSettings(board, false);
        LinearCongrentialPieceGenerator pieceGenerator = new LinearCongrentialPieceGenerator(settings);
        BoardEvaluator boardEvaluator = new TengstrandBoardEvaluator1(board.width, board.height);

        GameState result = new GameState(board, pieceGenerator, 0);
        return new Game(result, boardEvaluator, settings);

    }

    public TetrisAnalyzer(GameMessenger messenger) {
        this.messenger = messenger;
        addMouseMotionListener(this);
        setVisible(true);
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

/*
    public String export() {
        long rowsPerGame = games == 0 ? 0 : totalRows / games;

        return "game state:" +
                board() +
                "\n  seed: " + pieceGenerator.state() +

                "\n  cell step: " + cellStep() +
                "\n  filled cells total: " + format(cells) +
                "\n  filled cells distribution: [" + cells() + "]\n" +
                "\n  pieces/s: " + duration.xPerSeconds(moves);
    }

     */

    public void paintOffscreen(Graphics g) {
        g.setColor(Color.black);
        g.setFont(monospacedFont);

        GameState state = messenger.getGameState();
        String duration = "Duration: " + state.duration.asString();
        String framesPerSec = "Frames/s: " + state.duration.xPerSeconds(paintedFrames++);
        String pieces = "Pieces: " + format(state.moves);

        String games = "Games: " + format(state.games);
        String rows = "Rows: " + format(state.rows);
        String rowsPerGame = "Rows/game: " + state.rowsPerGame();
        String piecesPerSec = "Pieces/s: " + state.duration.xPerSeconds(state.moves);

        paintTexts(g, framesPerSec, duration, pieces, rows, "", games, rowsPerGame, piecesPerSec);
        //state.coloredBoard.ass

        repaint();
        sleep(20);
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }

    private void paintTexts(Graphics g, String... texts) {
        for (int row=0; row < texts.length; row++) {
            paintText(texts[row], row, g);
        }
    }

    private void paintText(String text, int row, Graphics g) {
        g.drawChars(text.toCharArray(), 0, text.length(), 20, 30 + 16 * row);
    }
}