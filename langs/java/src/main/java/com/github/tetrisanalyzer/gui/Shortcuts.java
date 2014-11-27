package com.github.tetrisanalyzer.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.github.tetrisanalyzer.gui.ZoomCalculator.ZoomWindow;

public class Shortcuts {
    private List<Stack<ZoomWindow>> shortcuts;

    public Shortcuts(Stack<ZoomWindow> windows) {
        shortcuts = new ArrayList<>(10);

        for (int i=0; i<10; i++) {
            shortcuts.add(copy(windows));
        }
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
}
