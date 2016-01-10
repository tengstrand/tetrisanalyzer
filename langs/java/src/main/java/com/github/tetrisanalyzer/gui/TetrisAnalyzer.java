package com.github.tetrisanalyzer.gui;

import com.github.tetrisanalyzer.FileChangeObserver;
import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.game.Distribution;
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

import static com.github.tetrisanalyzer.FileChangeObserver.FileChangedEvent;

public class TetrisAnalyzer extends JPanel implements KeyListener {

    private boolean paused;
    private Color actionColor = new Color(0,128,0);
    private ViewMode viewMode = ViewMode.DISTRIBUTION;

    private long actionAt;
    private long actionDuration;
    private String actionMessage;

    private String actionError;

    private Image offscreenImage;
    private RaceInfo raceInfo;
    private GraphBoardPainter graphBoardPainter;
    private RaceSettings race;
    private List<RaceGameSettings> games;
    private Graph graph;
    private JFrame frame;

    private String systemFilename;
    private String raceFilename;

    private static final int GRAPH_X1 = 50;
    private static final int GRAPH_Y1 = 330;

    private static Font monospacedFont = new Font("monospaced", Font.PLAIN, 12);

    /**
     * Incoming arguments for system and race settings, e.g.:
     *   /Users/joakimtengstrand/Source/Idea/tetrisanalyzer/langs/java/example/system.yaml /Users/joakimtengstrand/TetrisAnalyzer/race.yaml
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

        TetrisAnalyzer tetrisAnalyzer = new TetrisAnalyzer(systemFilename, raceFilename, frame);
        tetrisAnalyzer.saveOnClose(frame);
        tetrisAnalyzer.setViewMode(ViewMode.DISTRIBUTION);

        WindowLocation loc = tetrisAnalyzer.race.windowLocation;
        frame.pack();
        frame.setLocation(loc.x1, loc.y1);
        frame.setSize(loc.width, loc.height);
        frame.setVisible(true);
        frame.getContentPane().add(tetrisAnalyzer);

        tetrisAnalyzer.startGames();
    }

    public TetrisAnalyzer(String systemFilename, String raceFilename, JFrame frame) {
        this.systemFilename = systemFilename;
        this.raceFilename = raceFilename;

        reloadGames();
        startFileChangeObserver(raceFilename);

        this.frame = frame;
        games = race.games;

        addKeyListener(this);

        setFocusable(true);
        setVisible(true);

        setPreferredSize(new Dimension(300, 300));
    }

    private void startFileChangeObserver(String raceFilename) {
        if (race.restartOnFileChange) {
            FileChangedEvent event = new FileChangedEvent() {
                @Override
                public void changed() {
                    reloadAndRestartGames();
                }
            };
            new Thread(new FileChangeObserver(raceFilename, event)).start();
        }
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

        raceInfo.paintTexts(g, 0);

        int y1 = raceInfo.height();
        int x1 = 20;
        int height = frame.getHeight() - y1 - 60;
        if (height < 100) height = 100;
        int w1 = (int) ((frame.getWidth() - 70) * 0.85);
        int w2 = (int) ((frame.getWidth() - 70) * 0.15) - 20;

        g.setColor(Color.GRAY);
        if (viewMode == ViewMode.DISTRIBUTION) {
            raceInfo.paintTextAt(" " + viewMode.viewName, 2, 0, g);
        } else if (viewMode == ViewMode.AREAS || viewMode == ViewMode.GAMES){
            x1 = 210;
            raceInfo.paintTextAt(viewMode.viewName, 2, 2, g);
        }
        int x2 = w1 + 50;

        g.setColor(Color.lightGray);
        g.drawRect(x1, y1, w1, height);

        ZoomWindow window = graph.currentWindow();
        Distribution distribution = games.get(0).distribution;
        double row = distribution.boardHeight * window.x2;

        graph.draw(g, x1, y1, w1, height);
        if (viewMode == ViewMode.DISTRIBUTION) {
            if (!graph.isZoomed()) {
                paintAreaPercentBar(x1, y1, w1, height, g);
            }
            graphBoardPainter.paint(g, x2, y1, w2, height, row);
        }

        paintPaused(g);
        paintSaved(g);

        repaint();
    }

    private void paintAreaPercentBar(int x1, int y1, int width, int height, Graphics g) {
        g.setColor(Color.lightGray);

        int barX = x1 + (int)(width * (100 - race.areaPercentage) / 100.0);
        g.drawLine(barX, y1, barX, y1 + height);
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
        if (!paused && System.currentTimeMillis() - actionAt < actionDuration) {
            g.setColor(actionColor);
            raceInfo.paintTextAtColumn(actionMessage, 0, g);
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
                break;
            case 82: // <Ctrl> + R
                if (e.getModifiers() == 2) {
                    reloadAndRestartGames();
                }
                break;
            case 37: // Left
                race.increaseAreaPercentage();
                break;
            case 39: // Right
                race.decreaseAreaPercentage();
                break;
            case 113: // <F2>
                setViewMode(ViewMode.DISTRIBUTION);
                break;
            case 114: // <F3>
                setViewMode(ViewMode.AREAS);
                break;
            case 115: // <F4>
                setViewMode(ViewMode.GAMES);
                break;
            default:
                System.out.println("key: " + keyCode + " modifiers:" + e.getModifiers());
        }
    }

    private void setViewMode(ViewMode viewMode) {
        this.viewMode = viewMode;
        graph.setViewMode(viewMode);
    }

    private void reloadAndRestartGames() {
        stopGames();
        reloadGames();
        startGames();
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

    private void reloadGames() {
        SystemSettings systemSettings = SystemSettings.fromFile(systemFilename);
        race = RaceSettings.fromFile(raceFilename, systemSettings);

        for (RaceGameSettings settings : race.games) {
            Game game = settings.createGame(settings.tetrisRules);
            settings.thread = new Thread(game);
        }
        games = race.games;
        graph = new Graph(GRAPH_X1, GRAPH_Y1, games, race.shortcuts);
        raceInfo = new RaceInfo(race);
        Board board = games.get(0).gameState.board;
        graphBoardPainter = new GraphBoardPainter(board.width, board.height);

        paused = false;
        actionMessage = "";

        addKeyListener(graph);
        addMouseListener(graph);
        addMouseMotionListener(graph);
    }

    private void startGames() {
        for (RaceGameSettings settings : race.games) {
            settings.thread.start();
        }
    }

    private void setAction(String message, double seconds) {
        actionMessage = message;
        actionDuration = (long) (seconds * 1000);
        actionAt = System.currentTimeMillis();
    }

    private void pauseGames(boolean paused) {
        for (RaceGameSettings settings : games) {
            settings.game.paused = paused;
        }
    }

    private void waitForGamesToPause() {
        boolean allWaiting = false;
        while (!allWaiting) {
            allWaiting = true;
            for (RaceGameSettings settings : games) {
                allWaiting &= settings.game.waiting;
            }
        }
    }

    private void stopGames() {
        boolean allStopped = false;
        while (!allStopped) {
            allStopped = true;
            for (RaceGameSettings settings : games) {
                settings.game.stopThread();
                allStopped &= settings.game.stopped;
            }
        }
    }


    private void saveOnClose(JFrame frame) {
        if (race.saveOnClose) {
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    save();
                }
            });
        }
    }
}