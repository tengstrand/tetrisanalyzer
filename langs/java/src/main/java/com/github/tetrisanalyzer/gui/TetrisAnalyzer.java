package com.github.tetrisanalyzer.gui;

import com.github.tetrisanalyzer.game.Game;
import com.github.tetrisanalyzer.settings.RaceGameSettings;
import com.github.tetrisanalyzer.settings.RaceSettings;
import com.github.tetrisanalyzer.settings.SystemSettings;
import com.github.tetrisanalyzer.text.RaceInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

public class TetrisAnalyzer extends JPanel implements KeyListener {

    private boolean paused;
    private Color actionColor = new Color(0,128,0);

    private long savedAt;
    private String saveFilename;
    private String saveError;

    private Image offscreenImage;
    private final RaceInfo raceInfo;
    private RaceSettings race;
    private List<RaceGameSettings> games;
    private List<Color> colors;
    private Graph graph;

    private static final int GRAPH_X0 = 50;
    private static final int GRAPH_Y0 = 230;
    private static final int GRAPH_WIDTH = 600;
    private static final int GRAPH_HEIGHT = 300;

    private static Font monospacedFont = new Font("monospaced", Font.PLAIN, 12);

    /**
     * Incoming arguments for system and race settings, e.g.:
     *   C:/TetrisAnalyzer/system.yaml C:/TetrisAnalyzer/race/race.yaml
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Expected to find two arguments, e.g.: system.yaml race.yaml");
            return;
        }
        String systemFilename = args[0];
        String raceFilename = args[1];

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Tetris Analyzer 3.0 - by Joakim Tengstrand");
        frame.setLayout(new GridLayout());

        SystemSettings systemSettings = SystemSettings.fromFile(systemFilename);
        final RaceSettings race = RaceSettings.fromFile(raceFilename, systemSettings);

        if (race.games.size() == 0) {
            System.out.println("Could not find any games for the race!");
            return;
        }

        frame.setSize(750, 600);
        frame.setLocation(100, 200);
        frame.setVisible(true);

        saveOnClose(frame, race);

        Graph graph = graph(race.games);

        frame.getContentPane().add(new TetrisAnalyzer(graph, race));

        for (RaceGameSettings settings : race.games) {
            Game game = settings.createGame(race.tetrisRules);
            new Thread(game).start();
        }
    }

    private static Graph graph(List<RaceGameSettings> games) {
        return new Graph(GRAPH_X0, GRAPH_Y0, GRAPH_WIDTH, GRAPH_HEIGHT, games);
    }

    public TetrisAnalyzer(Graph graph, RaceSettings race) {
        this.graph = graph;

        this.race = race;
        this.games = race.games;
        this.colors = race.colors;
        this.raceInfo = new RaceInfo(race.games);

        addKeyListener(this);
        addKeyListener(graph);
        addMouseListener(graph);
        addMouseMotionListener(graph);

        this.setFocusable(true);
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
        sleep(50);
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
        graph.draw(g);
        paintPaused(g);
        paintSaved(g);

        repaint();
    }

    private void paintPaused(Graphics g) {
        if (!paused) {
            return;
        }
        g.setColor(actionColor);
        raceInfo.paintTextAtColumn("Paused", 0, g);
    }

    private void paintSaved(Graphics g) {
        if (saveError != null) {
            g.setColor(Color.red);
            raceInfo.paintTextAtColumn(saveError, 1, g);
        }
        if (System.currentTimeMillis() - savedAt < 2000) {
            g.setColor(actionColor);
            raceInfo.paintTextAtColumn("Saved: " + saveFilename, 1, g);
        }
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case 80: // P
                togglePaused();
                break;
            case 83: // S
                save();
                savedAt = System.currentTimeMillis();
                break;
            default:
                System.out.println("key: " + keyCode);
        }
    }

    private void togglePaused() {
        paused = !paused;
        pauseGames(paused);
    }

    private void save() {
        boolean paused = this.paused;
        pauseGames(true);
        sleep(110);

        try {
            saveFilename = race.saveToFile();
        } catch (IOException e) {
            saveError = e.getLocalizedMessage();
        }
        pauseGames(paused);
    }

    private void pauseGames(boolean paused) {
        for (RaceGameSettings game : games) {
            game.game.paused = paused;
        }
    }

    private static void saveOnClose(JFrame frame, final RaceSettings race) {
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    race.saveToFile();
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
            }
        });
    }
}