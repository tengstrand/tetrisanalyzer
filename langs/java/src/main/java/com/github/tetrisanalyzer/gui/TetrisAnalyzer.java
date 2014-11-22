package com.github.tetrisanalyzer.gui;

import com.github.tetrisanalyzer.game.Game;
import com.github.tetrisanalyzer.settings.RaceGameSettings;
import com.github.tetrisanalyzer.settings.RaceSettings;
import com.github.tetrisanalyzer.settings.SystemSettings;
import com.github.tetrisanalyzer.text.RaceInfo;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TetrisAnalyzer extends JPanel {

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
        double start = 0.1;
        double end = 0.15;

        Graph multiGraph = multiGraph(start, end, race.games);
        Graph overviewGraph = overviewGraph(numberOfCells, start, end, race.games);

        frame.getContentPane().add(new TetrisAnalyzer(multiGraph, overviewGraph, raceInfo, race.games, colors));

        List<Game> games = new ArrayList<>();

        for (RaceGameSettings settings : race.games) {
            Game game = settings.createGame(race.tetrisRules);
            games.add(game);
            new Thread(game).start();
        }
    }

    private static Graph multiGraph(double start, double end, List<RaceGameSettings> games) {
        return new Graph(DIST_X0, DIST_Y0, DIST_WIDTH, DIST_HEIGHT, start, end, games);
    }

    private static Graph overviewGraph(int numberOfCells, double startSelection, double endSelection, List<RaceGameSettings> games) {
        int selectedGraphIndex = games.size() / 2;
        List<RaceGameSettings> game = Arrays.asList(games.get(selectedGraphIndex));

        int width = (int)(DIST_WIDTH * 0.7);
        int height = (int)(DIST_HEIGHT * 0.7);
        int endIdx = (int)((numberOfCells - 1) * 0.5);

        return new Graph(750, DIST_Y0 + 50, width, height, 0, endIdx, startSelection, endSelection, game);
    }

    public TetrisAnalyzer(Graph multiGraph, Graph overviewGraph, RaceInfo raceInfo, List<RaceGameSettings> games, List<Color> colors) {
        this.multiGraph = multiGraph;
        this.overviewGraph = overviewGraph;
        this.raceInfo = raceInfo;
        this.games = games;
        this.colors = colors;
        addMouseListener(multiGraph);
        addMouseMotionListener(multiGraph);
        addMouseListener(overviewGraph);
        addMouseMotionListener(overviewGraph);
        setVisible(true);

        setPreferredSize(new Dimension(300, 300));
    }

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
        overviewGraph.draw(g);

        overviewGraph.setSelection(multiGraph);

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