package com.github.tetrisanalyzer.game;

import java.util.Locale;

public class Duration {
    public final long startMillis;
    public final long endMillis;

    public final int days;
    public final int hours;
    public final int minutes;
    public final int seconds;
    public final int millis;

    public Duration(long startMillis, long endMillis) {
        if (endMillis < startMillis) {
            throw new IllegalArgumentException("End time is before start time");
        }

        this.startMillis = startMillis;
        this.endMillis = endMillis;

        final long millisPerDay = 86400000;
        final long millisPerHour = 3600000;
        final long millisPerMinute = 60000;
        final long millisPerSecond = 1000;

        long duration = endMillis - startMillis;

        days = (int) (duration / millisPerDay);
        duration = (duration % millisPerDay);
        hours = (int)(duration / millisPerHour);
        duration = (duration % millisPerHour);
        minutes = (int)(duration / millisPerMinute);
        duration = (duration % millisPerMinute);
        seconds = (int)(duration / millisPerSecond);
        millis = (int)(duration % millisPerSecond);
    }

    public static long currentTime() {
        return System.currentTimeMillis();
    }

    public static Duration create() {
        return create(System.currentTimeMillis());
    }

    public static Duration create(long startMillis) {
        return new Duration(startMillis, System.currentTimeMillis());
    }

    public static Duration create(long startMillis, long endMillis) {
        return new Duration(startMillis, endMillis);
    }

    public Duration stop() {
        return new Duration(startMillis, currentTime());
    }

    public double seconds() {
        return (endMillis - startMillis) / 1000.0;
    }

    private String round(double value) {
        return String.format(Locale.ENGLISH, "%.2f", value);
    }

    public String xPerSeconds(long x) {
        double seconds = seconds();

        double xPerSec = x / seconds;

        if (xPerSec >= 1000) {
            return StringUtils.format((long)xPerSec);
        }

        return seconds == 0 ? "0" : round(xPerSec);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Duration duration = (Duration) o;

        if (days != duration.days) return false;
        if (hours != duration.hours) return false;
        if (minutes != duration.minutes) return false;
        if (seconds != duration.seconds) return false;
        if (millis != duration.millis) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = days;
        result = 31 * result + hours;
        result = 31 * result + minutes;
        result = 31 * result + seconds;
        result = 31 * result + millis;
        return result;
    }

    public String asString() {
        return String.format("%dd %dh %dm %d.%03ds", days, hours, minutes, seconds, millis);
    }

    @Override
    public String toString() {
        return asString();
    }
}