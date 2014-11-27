package com.github.tetrisanalyzer.gui;

public class Zoomer {
    private final ZoomWindow zoomFrom;
    private final ZoomWindow zoomTo;
    private int step = 0;
    private int fullsizesteps;
    private final int steps;

    public Zoomer(ZoomWindow zoomFrom, ZoomWindow zoomTo, int steps) {
        this.zoomFrom = zoomFrom;
        this.zoomTo = zoomTo;
        this.steps = steps;
        this.fullsizesteps = steps / 4;
    }

    public boolean isZooming() {
        return step <= steps * 2;
    }

    public ZoomWindow zoom(int step) {
        this.step = step - 1;
        return zoom();
    }

    public ZoomWindow zoom() {
        step++;
        if (step < 0) {
            return zoomFrom;
        }
        if (step > steps * 2) {
            return zoomTo;
        }
        if (step < steps) {
            return zoomOut(step);
        }
        if (step == steps && fullsizesteps > 0) {
            fullsizesteps--;
            step--;
        }
        return zoomIn(step - steps);
    }

    private ZoomWindow zoomOut(int step) {
        return new ZoomWindow(
                zoomFrom.x1 / steps * (steps - step),
                zoomFrom.y1 / steps * (steps - step),
                zoomFrom.x2 + (1 - zoomFrom.x2) / steps * step,
                zoomFrom.y2 + (1 - zoomFrom.y2) / steps * step);
    }

    private ZoomWindow zoomIn(int step) {
        return new ZoomWindow(
                zoomTo.x1 / steps * step,
                zoomTo.y1 / steps * step,
                1 - (1 - zoomTo.x2) / steps * step,
                1 - (1 - zoomTo.y2) / steps * step);
    }
}
