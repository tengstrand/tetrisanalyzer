package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.game.Distribution;
import com.github.tetrisanalyzer.game.Duration;

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

        List<Integer> result = new ArrayList<>();

        for (Object value : (List) get(key)) {
            result.add(toInt(key, value));
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


    public List<Map> readMaps(String key) {
        ensureExists(key);
        ensureType(key, List.class);

        List<Map> result = new ArrayList<>();

        for (Object value : (List) get(key)) {
            result.add(toMap(key, value));
        }
        return result;
    }

    public List<Integer> readIntegers(String key, int expectedSize) {
        List<Integer> numbers = readIntegers(key);
        ensureSize(key, numbers, expectedSize);

        return numbers;
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
        Object obj = get("distribution");
        int xx = 1;
        return (Distribution)get("distribution");
    }

    public int readBoardWidth() {
        if (!exists("board")) {
            return 10;
        }
        return readIntegers("board", 2).get(0);
    }

    public int readBoardHeight() {
        if (!exists("board")) {
            return 20;
        }
        return readIntegers("board", 2).get(1);
    }

    public ColoredBoard readBoard() {
        if (!exists("board")) {
            return null;
        }
        List<Integer> board = readIntegers("board", 2);
        return ColoredBoard.create(board.get(0), board.get(1));
    }

    @Override
    public String toString() {
        return settings.toString();
    }
}
