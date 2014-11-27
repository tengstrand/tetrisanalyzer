package com.github.tetrisanalyzer.gui;

import javax.swing.*;

public class WindowLocation {
    public final int x1;
    public final int y1;
    public final int width;
    public final int height;

    public WindowLocation() {
        x1 = 100;
        y1 = 200;
        width = 750;
        height = 600;
    }

    public WindowLocation(java.util.List<Integer> values) {
        x1 = values.get(0);
        y1 = values.get(1);
        width = values.get(2);
        height = values.get(3);
    }

    public WindowLocation(JFrame frame) {
        this.x1 = frame.getX();
        this.y1 = frame.getY();
        this.width = frame.getWidth();
        this.height = frame.getHeight();
    }

    public String export() {
        return "window-location: [" + x1 + "," + y1 + "," + width + "," + height + "]";
    }
}
