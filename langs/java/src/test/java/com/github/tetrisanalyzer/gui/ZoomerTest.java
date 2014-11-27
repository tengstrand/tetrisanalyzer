package com.github.tetrisanalyzer.gui;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ZoomerTest {

    @Test
    public void zoomOut() {
        ZoomWindow zoomFrom = new ZoomWindow(0.2, 0.3, 0.4, 0.5);
        ZoomWindow zoomTo = new ZoomWindow(0.1, 0.2, 0.3, 0.4);
        Zoomer zoomer = new Zoomer(zoomFrom, zoomTo, 100);

        ZoomWindow window = zoomer.zoom(10);

        assertEquals(new ZoomWindow(0.176, 0.264, 0.47200000000000003, 0.56), window);
    }

    @Test
    public void zoomIn() {
        ZoomWindow zoomFrom = new ZoomWindow(0.2, 0.3, 0.4, 0.5);
        ZoomWindow zoomTo = new ZoomWindow(0.1, 0.2, 0.3, 0.4);
        Zoomer zoomer = new Zoomer(zoomFrom, zoomTo, 100);

        ZoomWindow window = zoomer.zoom(110);

        assertEquals(new ZoomWindow(0.01, 0.02, 0.93, 0.94), window);
    }
}
