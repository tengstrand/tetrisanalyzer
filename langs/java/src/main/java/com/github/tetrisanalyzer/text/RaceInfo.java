package com.github.tetrisanalyzer.text;

import com.github.tetrisanalyzer.settings.RaceGameSettings;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RaceInfo {
    private final List<RaceGameSettings> raceGameSettingsList;

    private static final int X0 = 20;
    private static final int Y0 = 30;

    public RaceInfo(List<RaceGameSettings> raceGameSettingsList) {
        this.raceGameSettingsList = raceGameSettingsList;
    }

    public List<String> parameters() {
        List<String> result = new ArrayList<>();

        for (RaceGameSettings settings : raceGameSettingsList) {
            result.add(settings.parameterValue.toString());
        }
        return result;
    }

    public RowsResult rows() {
        String[] rows = new String[10];

        final int paramLength = 16;
        rows[0] = "parameter value:";
        rows[1] = "----------------";
        rows[2] = rpad("duration:", paramLength);
        rows[3] = rpad("games:", paramLength);
        rows[4] = rpad("rows:", paramLength);
        rows[5] = rpad("", paramLength);
        rows[6] = rpad("rows/game:", paramLength);
        rows[7] = rpad("min rows:", paramLength);
        rows[8] = rpad("max rows:", paramLength);
        rows[9] = rpad("pieces/s:", paramLength);

        String[] values = new String[9];

        int parameterIdx = 0;
        String[] parameters = new String[raceGameSettingsList.size()];
        for (int i=0; i<parameters.length; i++) {
            parameters[i] = spaces(paramLength);
        }

        for (RaceGameSettings settings : raceGameSettingsList) {
            values[0] = settings.parameterValue.toString();
            values[1] = settings.gameState.duration.asDaysHoursMinutesSecs();
            values[2] = format(settings.gameState.games);
            values[3] = format(settings.gameState.rows);
            values[4] = "";
            values[5] = settings.gameState.rowsPerGame();
            values[6] = settings.gameState.minRows();
            values[7] = settings.gameState.maxRows();
            values[8] = settings.gameState.piecesPerSecond();

            int max = maxValueLength(values);
            rows[0] += "  " + lpad("", max);
            rows[1] += "  " + separator(max);
            rows[2] += "  " + lpad(values[1], max);
            rows[3] += "  " + lpad(values[2], max);
            rows[4] += "  " + lpad(values[3], max);
            rows[5] += "  " + lpad(values[4], max);
            rows[6] += "  " + lpad(values[5], max);
            rows[7] += "  " + lpad(values[6], max);
            rows[8] += "  " + lpad(values[7], max);
            rows[9] += "  " + lpad(values[8], max);

            for (int i=0; i<parameters.length; i++) {
                if (i == parameterIdx) {
                    parameters[parameterIdx] += "  " + lpad(values[0], max);
                } else if (i <= parameterIdx) {
                    parameters[parameterIdx] += spaces(max + 2);
                }
            }
            parameterIdx++;
        }
        return new RowsResult(rows, parameters);
    }

    private int maxValueLength(String[] values) {
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
        int row = 11;
        g.drawChars(text.toCharArray(), 0, text.length(), X0 + 100 * column, Y0 + 16 * row);
    }

    public void paintTexts(Graphics g, int startRow, List<Color> colors) {
        RowsResult result = rows();

        for (int row=0; row < result.rows.length; row++) {
            paintText(result.rows[row], startRow + row, g);
        }

        for (int i=0; i<result.parameters.length; i++) {
            g.setColor(colors.get(i % colors.size()));
            g.drawChars(result.parameters[i].toCharArray(), 0, result.parameters[i].length(), X0, Y0);
        }
    }

    private void paintText(String text, int row, Graphics g) {
        g.drawChars(text.toCharArray(), 0, text.length(), X0, Y0 + 16 * row);
    }

    static class RowsResult {
        public String[] rows;
        public String[] parameters;

        RowsResult(String[] rows, String[] parameters) {
            this.rows = rows;
            this.parameters = parameters;
        }
    }
}
