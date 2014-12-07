package com.github.tetrisanalyzer.game;

public class XPerLastSecond {
    private long startMillis;
    private long startX;
    public int xPerSecond;

    public XPerLastSecond(long startMillis, long x) {
        this.startMillis = startMillis;
        this.startX = x;
    }

    public void update(long millis, long x) {
        long duration = millis - startMillis;

        if (duration >= 1000) {
            xPerSecond = (int) (((x - startX) * 1000) / duration);
            startMillis = millis;
            startX = x;
        }
    }
}
