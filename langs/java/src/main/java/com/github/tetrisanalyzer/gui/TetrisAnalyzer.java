package com.github.tetrisanalyzer.gui;

import com.github.tetrisanalyzer.game.Game;
import com.github.tetrisanalyzer.settings.RaceGameSettings;
import com.github.tetrisanalyzer.settings.RaceSettings;
import com.github.tetrisanalyzer.settings.SystemSettings;
import com.github.tetrisanalyzer.text.RaceInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class TetrisAnalyzer extends JPanel implements MouseListener {

    private Image offscreenImage;
    private final RaceInfo raceInfo;
    private List<RaceGameSettings> games;
    private List<Color> colors;

    private int startIdx1;
    private int endIdx1;

    private int startIdx2;
    private int endIdx2;

    private final int DIST_X0 = 50;
    private final int DIST_Y0 = 250;
    private final int DIST_WIDTH = 600;
    private final int DIST_HEIGHT = 300;
    private final int DIST_X1 = DIST_X0 + DIST_WIDTH - 1;
    private final int DIST_Y1 = DIST_Y0 + DIST_HEIGHT - 1;
    private final int DIST_X_MID = (DIST_X0 + DIST_X1) / 2;

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

        if (race.games.size() == 0) {
            throw new IllegalArgumentException("Could not find any games for the race");
        }

        frame.setSize(1300, 650);
        frame.setLocation(100, 300);
        frame.setVisible(true);

        List<Color> colors = new ArrayList<>();
        for (RaceGameSettings settings : race.games) {
            colors.add(settings.color);
        }

        RaceInfo raceInfo = new RaceInfo(race.games);
        int numberOfCells = race.games.get(0).distribution.cells.length;
        int startIdx1 = (int)(numberOfCells * 0.1);
        int endIdx1 =  (int)(numberOfCells * 0.15);
        int startIdx2 = 0;
        int endIdx2 = (int)((numberOfCells - 1) * 0.5);

        frame.getContentPane().add(new TetrisAnalyzer(startIdx1, endIdx1, startIdx2, endIdx2, raceInfo, race.games, colors));

        List<Game> games = new ArrayList<>();

        for (RaceGameSettings settings : race.games) {
            Game game = settings.createGame(race.tetrisRules);
            games.add(game);
            new Thread(game).start();
        }
    }

    public TetrisAnalyzer(int startIdx1, int endIdx1, int startIdx2, int endIdx2, RaceInfo raceInfo, List<RaceGameSettings> games, List<Color> colors) {
        this.startIdx1 = startIdx1;
        this.endIdx1 = endIdx1;
        this.startIdx2 = startIdx2;
        this.endIdx2 = endIdx2;
        this.raceInfo = raceInfo;
        this.games = games;
        this.colors = colors;
        addMouseListener(this);
        setVisible(true);

        setPreferredSize(new Dimension(300, 300));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        adjustGraph(e.getX(), e.getY());
    }

    private void adjustGraph(int x, int y) {
        if (y < DIST_Y0 || y > DIST_Y1) {
            return;
        }
        int dx1 = 0;
        int dx2 = 0;
        if (x < DIST_X0) {
            dx1 = -1;
        } else if (x < DIST_X_MID) {
            dx1 = 1;
        } else if (x > DIST_X1) {
            dx2 = 1;
        } else {
            dx2 = -1;
        }

        RaceGameSettings game = games.get(0);
        int startIdx = this.startIdx1 + dx1;
        int endIdx = this.endIdx1 + dx2;
        int maxIdx = game.distribution.cells.length - 1;
        if (startIdx < 0) { startIdx = 0; }
        if (endIdx < 0) { endIdx = 0; }
        if (startIdx > maxIdx) { startIdx = maxIdx; }
        if (endIdx > maxIdx) { endIdx = maxIdx; }
        if (startIdx > endIdx) {
            int idx = startIdx;
            startIdx = endIdx;
            endIdx = idx;
        }
        this.startIdx1 = startIdx;
        this.endIdx1 = endIdx;
    }

    @Override public void mouseClicked(MouseEvent me) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        Dimension d = getSize();
        checkOffscreenImage();
        Graphics offG = offscreenImage.getGraphics();
        offG.setColor(Color.white);
        offG.fillRect(0, 0, d.width, d.height);

        paintOffscreen(offscreenImage.getGraphics());
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

        raceInfo.paintTexts(g, 0, colors);

        int index = games.size() / 2;
        for (RaceGameSettings game : games) {
            g.setColor(game.color);
            game.distribution.lines(startIdx1, endIdx1, DIST_WIDTH, DIST_HEIGHT).draw(DIST_X0, DIST_Y0, g);

            if (index-- == 0) {
                g.setColor(Color.gray);
                int width = (int)(DIST_WIDTH * 0.7);
                int height = (int)(DIST_HEIGHT * 0.7);
                game.distribution.lines(startIdx2, endIdx2, width, height).draw(750, DIST_Y0 + 50, startIdx1, endIdx1, height, g);
            }
        }
        repaint();
        sleep(20);
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }
}