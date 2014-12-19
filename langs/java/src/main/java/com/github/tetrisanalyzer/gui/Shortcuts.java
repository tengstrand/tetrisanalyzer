package com.github.tetrisanalyzer.gui;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import static java.util.Map.Entry;

public class Shortcuts {
    private List<Stack<ZoomWindow>> shortcuts;

    public Shortcuts() {
        Stack<ZoomWindow> windows = new Stack<>();
        windows.push(new ZoomWindow(0,0,1,1));

        shortcuts = new ArrayList<>(10);
        for (int i=0; i<10; i++) {
            shortcuts.add(copy(windows));
        }
    }

    public Shortcuts(Map<String,Map> keys) {
        this();

        for (Entry<String,Map> entry : keys.entrySet()) {
            int keyIndex = index(entry.getKey().toString());
            Stack<ZoomWindow> windows = new Stack<>();
            windows.push(new ZoomWindow());

            Map<String,List> levels = entry.getValue();

            // The entries can be in wrong order so we need to put them in a tree map first.
            Map<Integer,ZoomWindow> levelsMap = new TreeMap<>();
            for (Entry<String,List> levelEntry : levels.entrySet()) {
                int levelIndex = index(levelEntry.getKey().toString());
                List<String> v = levelEntry.getValue();
                levelsMap.put(levelIndex, new ZoomWindow(value(v,0), value(v, 1), value(v, 2), value(v, 3)));
            }
            for (ZoomWindow window : levelsMap.values()) {
                windows.push(window);
            }
            shortcuts.set(keyIndex, windows);
        }
    }

    private int index(String key) {
        int index = key.indexOf("-");
        return Integer.parseInt(key.substring(index + 1, key.length()));
    }

    private double value(List<String> list, int index) {
        return Double.parseDouble(list.get(index));
    }

    public Stack<ZoomWindow> get(int index) {
        return copy(shortcuts.get(index));
    }

    public void set(int index, Stack<ZoomWindow> windows) {
        shortcuts.set(index, copy(windows));
    }

    private Stack<ZoomWindow> copy(Stack<ZoomWindow> windows) {
        Stack<ZoomWindow> copy = new Stack<>();
        copy.addAll(windows);
        return copy;
    }

    public String export() {
        String result = "";

        for (int i=0; i<shortcuts.size(); i++) {
            Stack<ZoomWindow> stack = shortcuts.get(i);
            if (stack.size() > 1) {
                result += "  key-" + i + ":\n";

                Enumeration<ZoomWindow> elements = stack.elements();

                elements.nextElement();
                int level = 1;
                while (elements.hasMoreElements()) {
                    ZoomWindow w = elements.nextElement();
                    result += "    window-" + level + ": [" + w.x1 + ", " + w.y1 + ", " + w.x2 + ", " + w.y2 + "]\n";
                    level++;
                }
            }
        }
        return result.isEmpty() ? "" : "zoom windows:\n" + result;
    }
}
