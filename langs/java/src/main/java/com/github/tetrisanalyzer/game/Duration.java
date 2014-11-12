package com.github.tetrisanalyzer.game;

import java.util.Locale;

public class Duration {
    public long startMillis;
    public long endMillis;
    public long duration;

    public int days;
    public int hours;
    public int minutes;
    public int seconds;
    public int millis;

    private long millisPerDay = 86400000;
    private long millisPerHour = 3600000;
    private long millisPerMinute = 60000;
    private long millisPerSecond = 1000;

    /**
     * Example: 1d 3h 52m 10.760s
     */
    public Duration(String stringFormat) {
        String[] times = stringFormat.split(" ");

        if (times.length != 4) {
            throw new IllegalArgumentException("Illegal format, expected something like: 1d 3h 52m 10.760s");
        }

        String d = times[0];
        String h = times[1];
        String m = times[2];
        String s = times[3];

        if (!d.endsWith("d")) throwException(d);
        if (!h.endsWith("h")) throwException(h);
        if (!m.endsWith("m")) throwException(m);
        if (!s.endsWith("s")) throwException(s);

        days = (int)time(d);
        hours = (int)time(h);
        minutes = (int)time(m);
        seconds = (int)time(s);
        millis = (int)((time(s) - seconds + .00001) * 1000);

        duration = days * millisPerDay + hours * millisPerHour + minutes * millisPerMinute + seconds * millisPerSecond;

        endMillis = System.currentTimeMillis();
        startMillis = endMillis - duration;
    }

    private double time(String time) {
        try {
            return Double.parseDouble(time.substring(0, time.length() - 1));
        } catch (Exception e) {
            throwException(time);
            return 0;
        }
    }

    private void throwException(String time) {
        throw new IllegalArgumentException("Illegal format, expected something like: 1d 3h 52m 10.760s, but was: '" + time + "'");
    }

    public Duration(int days, int hours, int minutes, int seconds, int millis) {
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.millis = millis;

        duration = days * millisPerDay + hours * millisPerHour + minutes * millisPerMinute + seconds * millisPerSecond;

        endMillis = System.currentTimeMillis();
        startMillis = endMillis - duration;
    }

    public Duration(long startMillis, long endMillis) {
        if (endMillis < startMillis) {
            throw new IllegalArgumentException("End time is before start time");
        }
        this.startMillis = startMillis;
        initTime(endMillis);
    }

    private void initTime(long endMillis) {
        this.endMillis = endMillis;
        duration = endMillis - startMillis;

        days = (int) (duration / millisPerDay);
        duration = (duration % millisPerDay);
        hours = (int)(duration / millisPerHour);
        duration = (duration % millisPerHour);
        minutes = (int)(duration / millisPerMinute);
        duration = (duration % millisPerMinute);
        seconds = (int)(duration / millisPerSecond);
        millis = (int)(duration % millisPerSecond);
    }

    public void setEndTime() {
        initTime(System.currentTimeMillis());
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

    public String asDaysHoursMinutesSecs() {
        return String.format("%dd %dh %dm %2ds", days, hours, minutes, seconds);
    }

    @Override
    public String toString() {
        return asString();
    }
}