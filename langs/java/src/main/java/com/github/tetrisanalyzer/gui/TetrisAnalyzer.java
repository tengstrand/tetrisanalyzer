package com.github.tetrisanalyzer.gui;

import com.github.tetrisanalyzer.FileChangeObserver;
import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.game.Distribution;
import com.github.tetrisanalyzer.game.Game;
import com.github.tetrisanalyzer.gui.graph.AreasGraph;
import com.github.tetrisanalyzer.gui.graph.DistributionGraph;
import com.github.tetrisanalyzer.gui.graph.Graph;
import com.github.tetrisanalyzer.settings.RaceGameSettings;
import com.github.tetrisanalyzer.settings.RaceGamesSettings;
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
import java.util.Iterator;

import static com.github.tetrisanalyzer.FileChangeObserver.FileChangedEvent;

public class TetrisAnalyzer extends JPanel implements KeyListener {

    private boolean paused;
    private final static int CHAR_WIDTH = 8;
    private Color actionColor = new Color(0,128,0);
    private ViewMode viewMode = ViewMode.ROWS_PER_GAME;
    private ViewMode areasViewMode = ViewMode.DISTRIBUTION_AREA;

    private long actionAt;
    private long actionDuration;
    private String actionMessage;

    private String actionError;

    private Image offscreenImage;
    private RaceInfo raceInfo;
    private GraphBoardPainter graphBoardPainter;
    private RaceSettings race;
    private RaceGamesSettings games;
    private Graph graph;
    private Graph areasGraph;
    private Graph distributionGraph;
    private DistributionGraph miniatureGraph;
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

        reloadGames(false);
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
                    reloadAndRestartGames(false);
                }
            };
            new Thread(new FileChangeObserver(raceFilename, event)).start();
        }
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        raceInfo.charWidth = CHAR_WIDTH;

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


        paintGraph(g);

        paintPaused(g);
        paintSaved(g);

        repaint();
    }

    private void paintGraph(Graphics g) {
        int x1 = 210;
        int y1 = raceInfo.height();
        int height = frame.getHeight() - y1 - 60;
        if (height < 100) height = 100;
        int w1 = frame.getWidth() - 240;
        int charWidth = g.getFontMetrics().charWidth(' ');

        g.setColor(Color.GRAY);
        raceInfo.paintTextAt(viewMode.viewName, 2, 2, g);

        if (viewMode == ViewMode.DISTRIBUTION_AREA || viewMode == ViewMode.ROWS_PER_GAME) {
            w1 = raceInfo.totalWidth(charWidth);
        }

        g.setColor(Color.lightGray);
        g.drawRect(x1, y1, w1, height);

        graph.draw(viewMode, x1, y1, w1, height, g);

        int width = raceInfo.firstColumnWidth(charWidth) - 70;
        miniatureGraph.draw(null, 22, y1, width - 20, 50, g);

        miniatureGraph.drawSelection(distributionGraph.currentWindow(), g);

        Distribution distribution = games.get(0).distribution;
        paintBoard(20, y1 + 50, width, height - 50, distribution, g);
    }

    private void paintBoard(int x1, int y1, int width, int height, Distribution distribution, Graphics g) {
        if (distributionGraph.isZoomed() && viewMode == ViewMode.DISTRIBUTION) {
            double row = distribution.boardHeight * distributionGraph.currentWindow().x2;
            graphBoardPainter.paint(g, x1, y1, width, height, row);
        } else {
            double row = distribution.boardHeight * ((100 - race.areaPercentage) / 100.0);
            graphBoardPainter.paint(g, x1, y1, width, height, row);
        }
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
            case 72: // H
                if (raceInfo.showSelectedHeading && games.size() > 1) {
                    Game game = games.get(raceInfo.selectedHeadingColumn).game;
                    if (raceInfo.selectedHeadingColumn == games.size() - 1) {
                        raceInfo.selectedHeadingColumn--;
                    }
                    game.stopThread();
                    game.hide();
                }
                break;
            case 80: // P
                race.resetSpeedometer();
                if (raceInfo.showSelectedHeading) {
                    games.get(raceInfo.selectedHeadingColumn).game.togglePaused();
                } else {
                    togglePaused();
                }
                break;
            case 83: // S
                save();
                if (e.getModifiers() == 2) { // <Ctrl> + S
                    reloadAndRestartGames(true);
                }
                break;
            case 67:
                if (e.getModifiers() == 2) {
                    copyToClipboard(); // <Ctrl> + C
                } else if (e.getModifiers() == 3) {
                    copyBoardsToClipboard(); // <Ctrl> + <Shift> + C
                } else {
                    raceInfo.toggleShowHeading();
                }
                break;
            case 82:
                if (e.getModifiers() == 2) { // <Ctrl> + R
                    reloadAndRestartGames(false);
                }
                break;
            case 37: // Left
                if (raceInfo.showSelectedHeading) {
                    raceInfo.moveSelectedColumnLeft();
                } else {
                    race.decreaseAreaPercentage();
                }
                break;
            case 39: // Right
                if (raceInfo.showSelectedHeading) {
                    raceInfo.moveSelectedColumnRight();
                } else {
                    race.increaseAreaPercentage();
                }
                break;
            case 113: // <F2>
                setViewMode(ViewMode.DISTRIBUTION);
                break;
            case 114: // <F3>
                setViewMode(toggleViewMode());
                break;
            case 115: // <F4>
                setViewMode(ViewMode.EQUITY_DIFF);
                break;
            default:
                System.out.println("key: " + keyCode + " modifiers:" + e.getModifiers());
        }
    }

    // Set to 'Distribution' or toggle between 'Distribution area' and 'Rows per game'
    private ViewMode toggleViewMode() {
        if (viewMode == ViewMode.DISTRIBUTION) return areasViewMode;
        if (viewMode == ViewMode.ROWS_PER_GAME) return ViewMode.DISTRIBUTION_AREA;
        return ViewMode.ROWS_PER_GAME;
    }

    private void setViewMode(ViewMode view) {
        viewMode = view;

        switch (viewMode) {
            case DISTRIBUTION:
                updateListeners(distributionGraph, areasGraph);
                break;
            case DISTRIBUTION_AREA:
            case ROWS_PER_GAME:
            case EQUITY_DIFF:
                updateListeners(areasGraph, distributionGraph);
                break;
        }
    }

    private void updateListeners(Graph add, Graph remove) {
        graph = add;

        addKeyListener(add);
        addMouseListener(add);
        addMouseMotionListener(add);

        removeKeyListener(remove);
        removeMouseListener(remove);
        removeMouseMotionListener(remove);
    }

    private void reloadAndRestartGames(boolean showAll) {
        stopGames();
        reloadGames(showAll);
        startGames();
    }

    private void copyToClipboard() {
        copyToClipboard(race.export(new WindowLocation(frame), graph.export()));
    }

    private void copyBoardsToClipboard() {
        copyToClipboard(race.games.get(0).game.lastBoardsAsString());
    }

    private void copyToClipboard(String string) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        StringSelection strSel = new StringSelection(string);
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

    private void reloadGames(boolean showAll) {
        SystemSettings systemSettings = SystemSettings.fromFile(systemFilename);
        race = RaceSettings.fromFile(raceFilename, systemSettings, showAll);
        games = race.games;
        raceInfo = new RaceInfo(race);
        distributionGraph = new DistributionGraph(GRAPH_X1, GRAPH_Y1, false, race);
        areasGraph = new AreasGraph(GRAPH_X1, GRAPH_Y1, games, race.shortcuts);
        miniatureGraph = new DistributionGraph(GRAPH_X1, GRAPH_Y1, true, race);

        Board board = games.get(0).gameState.board;
        graphBoardPainter = new GraphBoardPainter(board.width, board.height);

        paused = false;
        actionMessage = "";

        updateListeners(distributionGraph, areasGraph);
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
            settings.game.temporarilyPaused = paused;
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