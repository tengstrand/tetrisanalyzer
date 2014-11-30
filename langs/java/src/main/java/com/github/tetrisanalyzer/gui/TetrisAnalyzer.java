package com.github.tetrisanalyzer.gui;

import com.github.tetrisanalyzer.game.Game;
import com.github.tetrisanalyzer.settings.RaceGameSettings;
import com.github.tetrisanalyzer.settings.RaceSettings;
import com.github.tetrisanalyzer.settings.SystemSettings;
import com.github.tetrisanalyzer.text.RaceInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

public class TetrisAnalyzer extends JPanel implements KeyListener {

    private boolean paused;
    private Color actionColor = new Color(0,128,0);

    private long actionAt;
    private long actionDuration;
    private String actionMessage;

    private String actionError;

    private Image offscreenImage;
    private final RaceInfo raceInfo;
    private RaceSettings race;
    private List<RaceGameSettings> games;
    private List<Color> colors;
    private Graph graph;
    private JFrame frame;

    private static final int GRAPH_X0 = 50;
    private static final int GRAPH_Y0 = 330;
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

        WindowLocation loc = race.windowLocation;
        frame.setLocation(loc.x1, loc.y1);
        frame.setSize(loc.width, loc.height);
        frame.setVisible(true);

        Graph graph = graph(race.games, race.shortcuts);

        TetrisAnalyzer tetrisAnalyzer = new TetrisAnalyzer(frame, graph, race);
        tetrisAnalyzer.saveOnClose(frame);

        frame.getContentPane().add(tetrisAnalyzer);

        for (RaceGameSettings settings : race.games) {
            Game game = settings.createGame(race.tetrisRules);
            new Thread(game).start();
        }
    }

    private static Graph graph(List<RaceGameSettings> games, Shortcuts shortcuts) {
        return new Graph(GRAPH_X0, GRAPH_Y0, GRAPH_WIDTH, GRAPH_HEIGHT, games, shortcuts);
    }

    public TetrisAnalyzer(JFrame frame, Graph graph, RaceSettings race) {
        this.frame = frame;
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
        if (actionError != null) {
            g.setColor(Color.red);
            raceInfo.paintTextAtColumn(actionError, 1, g);
        }
        if (System.currentTimeMillis() - actionAt < actionDuration) {
            g.setColor(actionColor);
            raceInfo.paintTextAtColumn(actionMessage, 1, g);
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
                break;
            case 67: // C
                copyToClipboard();
            default:
                System.out.println("key: " + keyCode);
        }
    }

    private void copyToClipboard() {
        String str = race.export(new WindowLocation(frame), graph.export());

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        StringSelection strSel = new StringSelection(str);
        clipboard.setContents(strSel, null);

        setAction("Copied to clipboard", 1);
    }

    private void togglePaused() {
        paused = !paused;
        pauseGames(paused);
    }

    private void save() {
        boolean paused = this.paused;
        pauseGames(true);
        waitForGamesToPause();

        try {
            setAction("Saved to: " + race.saveToFile(new WindowLocation(frame), graph.export()), 2);
        } catch (IOException e) {
            actionError = e.getLocalizedMessage();
        }
        pauseGames(paused);
    }

    private void setAction(String message, double seconds) {
        actionMessage = message;
        actionDuration = (long) (seconds * 1000);
        actionAt = System.currentTimeMillis();
    }

    private void pauseGames(boolean paused) {
        for (RaceGameSettings game : games) {
            game.game.paused = paused;
        }
    }

    private void waitForGamesToPause() {
        boolean allWaiting = false;
        while (!allWaiting) {
            allWaiting = true;
            for (RaceGameSettings game : games) {
                allWaiting &= game.game.waiting;
            }
        }
    }

    private void saveOnClose(JFrame frame) {
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                save();
            }
        });
    }
}