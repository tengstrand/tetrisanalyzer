package com.github.tetrisanalyzer.gui;

import com.github.tetrisanalyzer.game.GameMessage;
import com.github.tetrisanalyzer.game.GameState;
import com.github.tetrisanalyzer.settings.CustomSystemSettings;
import com.github.tetrisanalyzer.settings.RaceSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.FileNotFoundException;

import static com.github.tetrisanalyzer.game.StringUtils.format;

public class TetrisAnalyzer extends JPanel implements MouseMotionListener {

    private long paintedFrames;
    private Image offscreenImage;
    private final GameMessage message;

    private static Font monospacedFont = new Font("monospaced", Font.PLAIN, 12);

    public static void main(String[] args) throws FileNotFoundException {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Tetris Analyzer 3.0 - by Joakim Tengstrand");
        frame.setLayout(new GridLayout());

        String systemFilename = "C:/TetrisAnalyzer/settings/system.yaml";
        CustomSystemSettings systemSettings = CustomSystemSettings.fromFile(systemFilename);

        String raceFilename = "C:/TetrisAnalyzer/race/race.yaml";
        RaceSettings race = RaceSettings.fromFile(raceFilename, systemSettings);

        int xx = 1;
/*
        Game game = newGame(1);
        frame.getContentPane().add(new TetrisAnalyzer(game.message));

        frame.setSize(900, 650);
        frame.setLocation(300, 300);
        frame.setVisible(true);

        new Thread(game).start();
*/
/*
        new Thread(game1).start();
        new Thread(game2).start();
        new Thread(game3).start();
        new Thread(game4).start();
        new Thread(game5).start();
        new Thread(game6).start();
        new Thread(game7).start();
*/
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
        String size = "Board: " + state.board.width + " x " + state.board.height;
        String pieces = "Pieces: " + format(state.moves);

        String games = "Games: " + format(state.games);
        String rows = "Rows: " + format(state.rows);
        String rowsPerGame = "Rows/game: " + state.rowsPerGame();
        String minRows = "Min rows: " + state.minRows();
        String maxRows = "Max rows: " + state.maxRows();
        String cellsPerPos = "Cells/pos: " + round(state.numberOfCells / (double) state.moves);
        String piecesPerSec = "Pieces/s: " + state.duration.xPerSeconds(state.moves);

        paintTexts(g, 0, duration, framesPerSec, size, pieces, rows, rowsPerGame, minRows, maxRows, "", games, cellsPerPos, piecesPerSec);
        paintTexts(g, 13, message.board);

        repaint();
        sleep(20);
    }

    double round(double value) {
        return ((int)(value * 1000000)) / 1000000.0;
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