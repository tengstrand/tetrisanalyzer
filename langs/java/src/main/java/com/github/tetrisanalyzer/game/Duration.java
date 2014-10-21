package com.github.tetrisanalyzer.game;

public class Duration {
    final int days;
    final int hours;
    final int minutes;
    final int seconds;
    final int millis;

    public Duration(int days, int hours, int minutes, int seconds, int millis) {
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.millis = millis;
    }

    public static long currentTime() {
        return System.currentTimeMillis();
    }

    public static Duration duration(long startMillis, long endMillis) {
        final long millisPerDay = 86400000;
        final long millisPerHour = 3600000;
        final long millisPerMinute = 60000;
        final long millisPerSecond = 1000;

        long duration = endMillis - startMillis;

        int days = (int) (duration / millisPerDay);
        duration = (duration % millisPerDay);
        int hours = (int)(duration / millisPerHour);
        duration = (duration % millisPerHour);
        int minutes = (int)(duration / millisPerMinute);
        duration = (duration % millisPerMinute);
        int seconds = (int)(duration / millisPerSecond);
        int millis = (int)(duration % millisPerSecond);

        return new Duration(days, hours, minutes, seconds, millis);
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
        return String.format("%dd %dh %dm %d.%02ds", days, hours, minutes, seconds, millis / 10);
    }

    @Override
    public String toString() {
        return asString();
    }
}