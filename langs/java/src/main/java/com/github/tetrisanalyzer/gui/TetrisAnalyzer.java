package com.github.tetrisanalyzer.gui;

import com.github.tetrisanalyzer.game.Game;
import com.github.tetrisanalyzer.settings.RaceGameSettings;
import com.github.tetrisanalyzer.settings.RaceSettings;
import com.github.tetrisanalyzer.settings.SystemSettings;
import com.github.tetrisanalyzer.text.Parameters;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.FileNotFoundException;

public class TetrisAnalyzer extends JPanel implements MouseMotionListener {

    private long paintedFrames;
    private Image offscreenImage;
    private Parameters parameters;

    private static Font monospacedFont = new Font("monospaced", Font.PLAIN, 12);

    public static void main(String[] args) throws FileNotFoundException {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Tetris Analyzer 3.0 - by Joakim Tengstrand");
        frame.setLayout(new GridLayout());

        String systemFilename = "C:/TetrisAnalyzer/settings/system.yaml";
        SystemSettings systemSettings = SystemSettings.fromFile(systemFilename);

        String raceFilename = "C:/TetrisAnalyzer/race/race.yaml";
        RaceSettings race = RaceSettings.fromFile(raceFilename, systemSettings);

        Parameters parameters = new Parameters(race.games);

        frame.getContentPane().add(new TetrisAnalyzer(parameters));

        frame.setSize(900, 650);
        frame.setLocation(300, 300);
        frame.setVisible(true);

        for (RaceGameSettings settings : race.games) {
            Game game = new Game(settings.gameState, race.tetrisRules);
            new Thread(game).start();
        }
    }

    public TetrisAnalyzer(Parameters parameters) {
        this.parameters = parameters;
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

        paintTexts(g, 0, parameters.rows());
 //       paintTexts(g, 13, message.board);

        repaint();
        sleep(20);
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