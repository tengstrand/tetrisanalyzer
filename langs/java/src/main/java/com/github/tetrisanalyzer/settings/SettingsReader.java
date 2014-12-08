package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.game.Distribution;
import com.github.tetrisanalyzer.game.Duration;
import com.github.tetrisanalyzer.gui.Shortcuts;
import com.github.tetrisanalyzer.gui.WindowLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SettingsReader {
    private final Map settings;
    private final String group;

    public SettingsReader(Map settings, String group) {
        this.settings = settings;
        this.group = group;
    }

    public boolean readBoolean(String key, boolean defaultValue) {
        return hasValue(key) && !readString(key).isEmpty() ? readBoolean(key) : defaultValue;
    }

    public boolean readBoolean(String key) {
        ensureExists(key);
        try {
            return Boolean.parseBoolean(get(key).toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Attribute '" + key + "' in '" + group + "' must be 'true' or 'false', but was: " + get(key));
        }
    }

    public int readInteger(String key, int defaultValue) {
        return hasValue(key) && !readString(key).isEmpty() ? readInteger(key) : defaultValue;
    }

    public int readInteger(String key) {
        ensureExists(key);
        try {
            return Integer.parseInt(get(key).toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Attribute '" + key + "' in '" + group + "' must be an integer, but was: " + get(key));
        }
    }

    public long readLong(String key, long defaultValue) {
        return hasValue(key) ? readLong(key) : defaultValue;
    }

    public long readLong(String key) {
        ensureExists(key);
        try {
            return Long.parseLong(get(key).toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Attribute '" + key + "' in '" + group + "' must be a long, but was: " + get(key));
        }
    }

    public double readDouble(String key, double defaultValue) {
        return hasValue(key) ? readDouble(key) : defaultValue;
    }

    public double readDouble(String key) {
        ensureExists(key);
        try {
            return Double.parseDouble(get(key).toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Attribute '" + key + "' in '" + group + "' must be a double, but was: " + get(key));
        }
    }

    public String readString(String key) {
        ensureExists(key);
        return get(key).toString();
    }

    public Object readObject(String key) {
        ensureExists(key);
        return get(key);
    }

    public String readString(String key, String... validValues) {
        String result = readString(key);

        ensureValues(key, validValues);

        return result;
    }

    public List readList(String key) {
        ensureExists(key);
        ensureType(key, List.class);
        return (List)get(key);
    }

    public Map readMap(String key) {
        ensureExists(key);
        ensureType(key, Map.class);
        return (Map)get(key);
    }

    public Map readMap(String key, Map defaultMap) {
        if (!exists(key)) {
            return defaultMap;
        }
        ensureType(key, Map.class);
        return (Map)get(key);
    }

    public Class readClass(String key) {
        ensureExists(key);
        ensureType(key, String.class);

        try {
            return Class.forName((String)get(key));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Could not read class '" + key + "' in '" + group + "': " + e.getMessage());
        }
    }

    public List<Integer> readIntegers(String key) {
        ensureExists(key);
        ensureType(key, List.class);

        return asIntegers((List)get(key), key);
    }

    private List<Integer> asIntegers(List values, String key) {
        List<Integer> result = new ArrayList<>();

        for (Object value : values) {
            result.add(toInt(key, value));
        }
        return result;
    }

    public List<Integer> readIntegers(String key, int expectedSize, List<Integer> defaultValues) {
        if (!exists(key)) {
            return defaultValues;
        }
        return readIntegers(key, expectedSize);
    }

    public List<String> readStrings(String key) {
        ensureExists(key);
        ensureType(key, List.class);

        List<String> result = new ArrayList<>();

        for (Object value : (List) get(key)) {
            result.add(value.toString());
        }
        return result;
    }

    public List<Integer> readIntegers(String key, int expectedSize) {
        List<Integer> numbers = readIntegers(key);
        ensureSize(key, numbers, expectedSize);

        return numbers;
    }

    public Color readColor(String key, Color defaultColor) {
        if (!exists(key)) {
            return defaultColor;
        }
        return asColor(get(key).toString(), key);
    }

    public WindowLocation readWindowLocation(String key) {
        if (!exists(key)) {
            return new WindowLocation();
        }
        return new WindowLocation(readIntegers(key, 4));
    }

    private Color asColor(String rgb, String key) {
        if (rgb.length() != 6) {
            throw new IllegalArgumentException("Expected to find color in format 'RRGGBB' for attribute '" + key + "', but was: " + rgb);
        }
        return ColorConverter.color(rgb);
    }

    public List<Color> readColors(String key, List<Color> defaultColors) {
        if (!exists(key)) {
            return defaultColors;
        }
        List<String> colors = readStrings(key);
        List<Color> result = new ArrayList<>();

        for (String color : colors) {
            result.add(asColor(color, key));
        }
        return result;
    }

    public List<List> readLists(String key) {
        ensureExists(key);
        ensureType(key, List.class);

        List<List> result = new ArrayList<>();

        for (Object value : (List) get(key)) {
            result.add(toList(key, value));
        }
        return result;
    }

    public List<List> readLists(String key, List<List> defaultValue) {
        if (!exists(key)) {
            return defaultValue;
        }
        return readLists(key);
    }

    public List<Map> readMaps(String key) {
        ensureExists(key);
        ensureType(key, List.class);

        List<Map> result = new ArrayList<>();

        for (Object value : (List) get(key)) {
            result.add(toMap(key, value));
        }
        return result;
    }

    private int toInt(String key, Object value) {
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Elements of attribute '" + key + "' in '" + group + "' must be an integer, but was: " + value);
        }
    }

    private List toList(String key, Object value) {
        if (!(value instanceof List)) {
            throw new IllegalArgumentException("Elements of attribute '" + key + "' in '" + group + "' must be of type java.util.List, but was: " + value);
        }
        return (List)value;
    }

    private Map toMap(String key, Object value) {
        if (!(value instanceof Map)) {
            throw new IllegalArgumentException("Elements of attribute '" + key + "' in '" + group + "' must be of type java.util.Map, but was: " + value);
        }
        return (Map)value;
    }

    public Object get(String key) {
        return settings.get(key);
    }

    public boolean isEmpty(String key) {
        return readString(key).isEmpty();
    }

    public boolean exists(String key) {
        return settings.containsKey(key);
    }

    public boolean hasValue(String key) {
        return exists(key) && !isEmpty(key);
    }

    public void ensureValues(String key, String... validValues) {
        ensureExists(key);

        Object value = settings.get(key);

        for (String validValue : validValues) {
            if (value.equals(validValue)) {
                return;
            }
        }
        throw new IllegalArgumentException("Valid values for key '" + key + "' in '" + group + "' are: " + Arrays.asList(validValues) + ", but was: " + value);
    }

    public void ensureExists(String key) {
        if (!settings.containsKey(key)) {
            throw new IllegalArgumentException("Expected to find attribute '" + key + "' in '" + group + "'");
        }
    }

    public void ensureSize(String key, List list, int expectedSize) {
        if (!(list.size() == expectedSize)) {
            String elements = expectedSize <= 1 ? "element" : "elements";
            throw new IllegalArgumentException("Expected to find " + expectedSize + " " + elements + " in the list '" + key + "' in '" + group +
                    "', but was: " + get(key));
        }
    }

    private void ensureType(String key, Class clazz) {
        if (!clazz.isAssignableFrom(get(key).getClass())) {
            throw new IllegalArgumentException("Attribute '" + key + "' in '" + group + "' must be of type " + clazz.getCanonicalName() +
                "', but was: " + get(key));
        }
    }

    public Duration readDuration() {
        if (!exists("duration")) {
            return null;
        }
        return new Duration(readString("duration"));
    }

    public Distribution readDistribution(int boardWidth, int boardHeight) {
        if (!exists("distribution")) {
            return new Distribution(boardWidth, boardHeight);
        }
        List<String> list = (List)get("distribution");
        List<Long> cells = new ArrayList<>();
        for (String value : list) {
            cells.add(Long.parseLong(value));
        }
        return new Distribution(boardWidth, boardHeight, cells);
    }

    public ColoredBoard readBoard() {
        if (!exists("board")) {
            return null;
        }
        List board = readList("board");
        if (board.size() == 2) {
            List<Integer> values = asIntegers(board, "board");
            return ColoredBoard.create(values.get(0), values.get(1));
        } else {
            return ColoredBoard.createYaml(board);
        }
    }

    public Shortcuts readShortcuts(String key) {
        if (!exists(key)) {
            return new Shortcuts();
        }
        Map keys = readMap(key);

        return new Shortcuts(keys);
    }

    @Override
    public String toString() {
        return settings.toString();
    }
}
