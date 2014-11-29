package com.github.tetrisanalyzer.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class WindowZoomer {

    public static List<ZoomWindow> zoomOut(ZoomWindow from, ZoomWindow to, double zoomSpeed) {
        List<ZoomWindow> result = zoomIn(from, to, zoomSpeed);
        Collections.reverse(result);
        return result;
    }

    public static List<ZoomWindow> zoomIn(ZoomWindow from, ZoomWindow to, double zoomSpeed) {
        if (to == null) {
            return Collections.emptyList();
        }
        List<Double> zoomFactors = zoomFactors(from, to, zoomSpeed);
        List<Double> x1s = scale(zoomFactors, to.x1 - from.x1);
        List<Double> y1s = scale(zoomFactors, to.y1 - from.y1);
        List<Double> x2s = scale(zoomFactors, from.x2 - to.x2);
        List<Double> y2s = scale(zoomFactors, from.y2 - to.y2);

        List<ZoomWindow> windows = new ArrayList<>(zoomFactors.size());

        Iterator<Double> y1iterator = y1s.iterator();
        Iterator<Double> x2iterator = x2s.iterator();
        Iterator<Double> y2iterator = y2s.iterator();

        for (double x1 : x1s) {
            double y1 = y1iterator.next();
            double x2 = x2iterator.next();
            double y2 = y2iterator.next();
            windows.add(new ZoomWindow(from.x1 + x1, from.y1 + y1, from.x2 - x2, from.y2 - y2));
        }
        return windows;
    }

    private static List<Double> scale(List<Double> zoomFactors, double distance) {
        List<Double> result = new ArrayList<>();
        for (double zoomFactor : zoomFactors) {
            result.add(zoomFactor * distance);
        }
        return result;
    }

    private static List<Double> zoomFactors(ZoomWindow from, ZoomWindow to, double zoomSpeed) {
        if (zoomSpeed < 0 || zoomSpeed >= 1) {
            throw new IllegalArgumentException("The zoom speed must be in the range 0..1, but was:" + zoomSpeed);
        }
        double wratio = to.width() / from.width();
        double hratio = to.height() / from.height();
        double length;
        double target;
        double current;
        double start;
        if (wratio < hratio) {
            length = from.width() - to.width();
            target = to.width();
            current = start = from.width();
        } else {
            length = from.height() - to.height();
            target = to.height();
            current = start = from.height();
        }

        List<Double> line = new ArrayList<>();

        while (current > target) {
            line.add((start - current) / length);
            current *= zoomSpeed;
        }
        if (current < target) {
            line.add(1.0);
        }
        return line;
    }
}
