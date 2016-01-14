package com.github.tetrisanalyzer.text;

import com.github.tetrisanalyzer.game.GameState;
import com.github.tetrisanalyzer.settings.RaceGameSettings;
import com.github.tetrisanalyzer.settings.RaceSettings;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.github.tetrisanalyzer.text.StringFunctions.repeat;
import static com.github.tetrisanalyzer.text.StringFunctions.spaces;

public class RaceInfo {
    private final RaceSettings raceSettings;

    private static final int CHAR_HEIGHT = 16;
    private static final int X0 = 20;
    private static final int Y0 = 30;

    public RaceInfo(RaceSettings raceSettings) {
        this.raceSettings = raceSettings;
    }

    private List<String> text(int paramLength) {
        List<String> rows = new ArrayList<>();

        rows.add("parameter value:");
        rows.add("-------------------");
        rows.add(rpad("duration:", paramLength));
        rows.add(rpad("board:", paramLength));
        rows.add(rpad("tetris rules id:", paramLength));
        rows.add(rpad("piece generator id:", paramLength));
        rows.add(rpad("board evaluator id:", paramLength));
        rows.add(rpad("", paramLength));
        rows.add(rpad("games:", paramLength));
        rows.add(rpad("rows:", paramLength));
        rows.add(rpad("area (" + raceSettings.areaPercentage + "%):", paramLength));
        rows.add(rpad("", paramLength));
        rows.add(rpad("rows/game:", paramLength));
        rows.add(rpad("min rows:", paramLength));
        rows.add(rpad("max rows:", paramLength));
        rows.add(rpad("rows/s:", paramLength));
        rows.add(rpad("pieces/s:", paramLength));

        return rows;
    }

    private int textRows() {
        return text(19).size();
    }

    public RowsResult rows() {
        int column = 19;
        List<String> rows = text(column);

        List<Integer> columns = new ArrayList<>(raceSettings.games.size());

        for (RaceGameSettings settings : raceSettings.games) {
            List<String> values = new ArrayList<>();
            GameState state = settings.gameState;
            values.add("");
            values.add("");
            values.add(state.duration.asDaysHoursMinutesSecs());
            values.add(state.board.width + " x " + state.board.height);
            values.add(settings.tetrisRulesId);
            values.add(settings.pieceGeneratorId);
            values.add(settings.boardEvaluatorId);
            values.add("");
            values.add(state.games == 0 ? "" : format(state.games));
            values.add(format(state.rows));
            values.add(state.areaFormatted());
            values.add("");
            values.add(state.rowsPerGameFormatted());
            values.add(state.minRowsFormatted());
            values.add(state.maxRowsFormatted());
            values.add(state.rowsPerLastSecondsFormatted());
            values.add(state.piecesPerLastSecondsFormatted());

            int max = maxValueLength(values);
            column += 2 + max;
            columns.add(column);
            for (int i=0; i<rows.size(); i++) {
                if (i == 1) {
                    rows.set(i, rows.get(i) + "  " + separator(max));
                } else {
                    rows.set(i, rows.get(i) + "  " + lpad(values.get(i), max));
                }
            }
        }
        return new RowsResult(rows, columns);
    }

    private int maxValueLength(List<String> values) {
        int max = Integer.MIN_VALUE;
        for (String value : values) {
            if (value.length() > max) {
                max = value.length();
            }
        }
        return max;
    }

    private String format(long value) {
        return String.format("%,d", value).replace((char)160, (char)32);
    }

    private String rpad(String string, int length) {
        return string + spaces(length - string.length());
    }

    private String lpad(String string, int length) {
        return spaces(length - string.length()) + string;
    }

    private String separator(int n) {
        return repeat(n, "-");
    }

    public int translatePixeltoColumn(int x, int charWidth) {
        RowsResult rowsResult = rows();

        int x1 = X0 + rowsResult.columns.get(0).intValue() * charWidth;
        for (int i=1; i<rowsResult.columns.size(); i++) {
            int x2 = X0 + rowsResult.columns.get(i).intValue() * charWidth;
            if (x >= x1 && x < x2) {
                return i;
            }
        }
        return 0;
    }

    public int firstColumnWidth(int charWidth) {
        RowsResult rowsResult = rows();
        return rowsResult.columns.get(0).intValue() * charWidth;
    }

    public int totalWidth(int charWidth) {
        RowsResult rowsResult = rows();
        int size = rowsResult.columns.size();

        int columns = rowsResult.columns.get(size-1).intValue() - rowsResult.columns.get(0).intValue();

        return X0 + charWidth * columns;
    }

    public int height() {
        return Y0 + CHAR_HEIGHT * (textRows() + 3);
    }

    public void paintTextAtColumn(String text, int column, Graphics g) {
        paintTextAt(text, 0, column, g);
    }

    public void paintTextAt(String text, int rowNumber, int column, Graphics g) {
        int row = textRows() + rowNumber;
        g.drawChars(text.toCharArray(), 0, text.length(), X0 + 100 * column, Y0 + CHAR_HEIGHT * row);
    }

    public void paintTexts(Graphics g, int startRow) {
        RowsResult result = rows();

        for (int row=0; row < result.rows.size(); row++) {
            paintText(result.rows.get(row), startRow + row, g);
        }

        int charWidth = g.getFontMetrics().charWidth(' ');

        Iterator<RaceGameSettings> settingsIterator = raceSettings.games.iterator();
        for (int i=0; i<result.columns.size(); i++) {
            RaceGameSettings settings = settingsIterator.next();
            g.setColor(settings.color);
            String value = settings.heading();
            g.drawChars(value.toCharArray(), 0, value.length(), X0 + (result.columns.get(i) - value.length()) * charWidth, Y0);
        }
    }

    private void paintText(String text, int row, Graphics g) {
        g.drawChars(text.toCharArray(), 0, text.length(), X0, Y0 + CHAR_HEIGHT * row);
    }

    static class RowsResult {
        public List<String> rows;
        public List<Integer> columns;

        RowsResult(List<String> rows, List<Integer> columns) {
            this.rows = rows;
            this.columns = columns;
        }
    }
}
