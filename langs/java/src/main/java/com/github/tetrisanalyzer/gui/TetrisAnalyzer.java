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
import java.util.Arrays;
import java.util.List;

public class TetrisAnalyzer extends JPanel implements MouseListener {

    private Image offscreenImage;
    private final RaceInfo raceInfo;
    private List<RaceGameSettings> games;
    private List<Color> colors;
    private Graph multiGraph;
    private Graph overviewGraph;

    private static final int DIST_X0 = 50;
    private static final int DIST_Y0 = 250;
    private static final int DIST_WIDTH = 600;
    private static final int DIST_HEIGHT = 300;
    private static final int DIST_X1 = DIST_X0 + DIST_WIDTH - 1;
    private static final int DIST_Y1 = DIST_Y0 + DIST_HEIGHT - 1;
    private static final int DIST_X_MID = (DIST_X0 + DIST_X1) / 2;

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
        int startIdx = (int)(numberOfCells * 0.1);
        int endIdx =  (int)(numberOfCells * 0.15);

        Graph multiGraph = multiGraph(startIdx, endIdx, race.games);
        Graph overviewGraph = overviewGraph(numberOfCells, startIdx, endIdx, race.games);

        frame.getContentPane().add(new TetrisAnalyzer(multiGraph, overviewGraph, raceInfo, race.games, colors));

        List<Game> games = new ArrayList<>();

        for (RaceGameSettings settings : race.games) {
            Game game = settings.createGame(race.tetrisRules);
            games.add(game);
            new Thread(game).start();
        }
    }

    private static Graph multiGraph(int startIdx, int endIdx, List<RaceGameSettings> games) {
        return new Graph(DIST_X0, DIST_Y0, DIST_WIDTH, DIST_HEIGHT, startIdx, endIdx, games);
    }

    private static Graph overviewGraph(int numberOfCells, int startSelectionIdx, int endSelectionIdx, List<RaceGameSettings> games) {
        int index = games.size() / 2;
        List<RaceGameSettings> game = Arrays.asList(games.get(index));

        int width = (int)(DIST_WIDTH * 0.7);
        int height = (int)(DIST_HEIGHT * 0.7);
        int endIdx = (int)((numberOfCells - 1) * 0.5);

        return new Graph(750, DIST_Y0 + 50, width, height, 0, endIdx, startSelectionIdx, endSelectionIdx, game);
    }

    public TetrisAnalyzer(Graph multiGraph, Graph overviewGraph, RaceInfo raceInfo, List<RaceGameSettings> games, List<Color> colors) {
        this.multiGraph = multiGraph;
        this.overviewGraph = overviewGraph;
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

        multiGraph.adjustStartIndex(dx1);
        multiGraph.adjustEndIndex(dx2);
        overviewGraph.startSelectionIdx = multiGraph.startIdx;
        if (multiGraph.endIdx <= overviewGraph.endIdx) {
            overviewGraph.endSelectionIdx = multiGraph.endIdx;
        }
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
        multiGraph.draw(g);
        g.setColor(Color.gray);
        overviewGraph.draw(g);

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