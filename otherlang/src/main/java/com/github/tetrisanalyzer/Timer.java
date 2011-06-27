package com.github.tetrisanalyzer;

public class Timer {
    public static Timer timer1 = new Timer("timer1");
    public static Timer timer2 = new Timer("timer2");

    private long start;
    private String message;
    private long passed = 0;

    public static void printAllResult() {
        timer1.printResult();
    }

    public Timer(String message) {
        this.message = message;
    }

    public void start() {
        start = System.currentTimeMillis();
    }

    public void stop() {
        long stop = System.currentTimeMillis();
        passed += (stop - start);
        start = stop;
    }

    public void printResult() {
        System.out.println(message + ": " + (passed / 1000.0) + " sec");
    }
}
