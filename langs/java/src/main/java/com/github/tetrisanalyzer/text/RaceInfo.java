package com.github.tetrisanalyzer.text;

import com.github.tetrisanalyzer.settings.RaceGameSettings;

import java.util.List;

public class RaceInfo {
    private final List<RaceGameSettings> raceGameSettingsList;

    public RaceInfo(List<RaceGameSettings> raceGameSettingsList) {
        this.raceGameSettingsList = raceGameSettingsList;
    }

    public String[] rows() {
        String[] rows = new String[10];

        rows[0] = "parameter value:";
        rows[1] = "----------------";
        rows[2] = rpad("duration:", 16);
        rows[3] = rpad("games:", 16);
        rows[4] = rpad("rows:", 16);
        rows[5] = rpad("", 16);
        rows[6] = rpad("rows/game:", 16);
        rows[7] = rpad("min rows:", 16);
        rows[8] = rpad("max rows:", 16);
        rows[9] = rpad("pieces/s:", 16);

        String[] values = new String[9];

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
            rows[0] += "  " + lpad(values[0], max);
            rows[1] += "  " + separator(max);
            rows[2] += "  " + lpad(values[1], max);
            rows[3] += "  " + lpad(values[2], max);
            rows[4] += "  " + lpad(values[3], max);
            rows[5] += "  " + lpad(values[4], max);
            rows[6] += "  " + lpad(values[5], max);
            rows[7] += "  " + lpad(values[6], max);
            rows[8] += "  " + lpad(values[7], max);
            rows[9] += "  " + lpad(values[8], max);
        }
        return rows;
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
}
