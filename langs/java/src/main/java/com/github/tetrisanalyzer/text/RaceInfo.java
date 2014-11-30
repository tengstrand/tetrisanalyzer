package com.github.tetrisanalyzer.text;

import com.github.tetrisanalyzer.game.GameState;
import com.github.tetrisanalyzer.settings.RaceGameSettings;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RaceInfo {
    private final List<RaceGameSettings> raceGameSettingsList;

    private static final int X0 = 20;
    private static final int Y0 = 30;

    public RaceInfo(List<RaceGameSettings> raceGameSettingsList) {
        this.raceGameSettingsList = raceGameSettingsList;
    }

    public RowsResult rows() {
        List<String> rows = new ArrayList<>();

        final int paramLength = 19;
        rows.add("parameter value:");
        rows.add("-------------------");
        rows.add(rpad("duration:", paramLength));
        rows.add(rpad("board:", paramLength));
        rows.add(rpad("board evaluator id:", paramLength));
        rows.add(rpad("games:", paramLength));
        rows.add(rpad("rows:", paramLength));
        rows.add(rpad("", paramLength));
        rows.add(rpad("rows/game:", paramLength));
        rows.add(rpad("min rows:", paramLength));
        rows.add(rpad("max rows:", paramLength));
        rows.add(rpad("rows/s:", paramLength));
        rows.add(rpad("pieces/s:", paramLength));

        List<String> values = new ArrayList<>();
        int column = paramLength;
        List<Integer> columns = new ArrayList<>(raceGameSettingsList.size());

        for (RaceGameSettings settings : raceGameSettingsList) {
            GameState state = settings.gameState;
            values.add("");
            values.add("");
            values.add(state.duration.asDaysHoursMinutesSecs());
            values.add(state.board.width + " x " + state.board.height);
            values.add(settings.boardEvaluatorId);
            values.add(state.games == 0 ? "" : format(state.games));
            values.add(format(state.rows));
            values.add("");
            values.add(state.rowsPerGame());
            values.add(state.minRows());
            values.add(state.maxRows());
            values.add(state.rowsPerSecond());
            values.add(state.piecesPerSecond());

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

    private String spaces(int n) {
        return repeat(n, " ");
    }

    private String separator(int n) {
        return repeat(n, "-");
    }

    private String repeat(int n, String character) {
        return new String(new char[n]).replace("\0", character);
    }

    public void paintTextAtColumn(String text, int column, Graphics g) {
        int row = 12;
        g.drawChars(text.toCharArray(), 0, text.length(), X0 + 100 * column, Y0 + 16 * row);
    }

    public void paintTexts(Graphics g, int startRow, List<Color> colors) {
        RowsResult result = rows();

        for (int row=0; row < result.rows.size(); row++) {
            paintText(result.rows.get(row), startRow + row, g);
        }

        int charWidth = g.getFontMetrics().charWidth(' ');

        Iterator<RaceGameSettings> settingsIterator = raceGameSettingsList.iterator();
        for (int i=0; i<result.columns.size(); i++) {
            RaceGameSettings settings = settingsIterator.next();
            g.setColor(colors.get(i % colors.size()));
            String value = settings.parameterValue.toString();
            g.drawChars(value.toCharArray(), 0, value.length(), X0 + (result.columns.get(i) - value.length()) * charWidth, Y0);
        }
    }

    private void paintText(String text, int row, Graphics g) {
        g.drawChars(text.toCharArray(), 0, text.length(), X0, Y0 + 16 * row);
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
