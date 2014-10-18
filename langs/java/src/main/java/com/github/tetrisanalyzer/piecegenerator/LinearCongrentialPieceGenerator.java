package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.settings.PieceSettings;

/**
 * This piece generator mimics the behaviour of the C++ version,
 * of TetrisAnalyzer that uses 32 bit unsigned integers.
 */
public class LinearCongrentialPieceGenerator extends PieceGenerator {
    private static long BIT_MASK = 0x00000000FFFFFFFFL;

    private long seed;
    private final long a;
    private final long b;

    public LinearCongrentialPieceGenerator(PieceSettings settings) {
        this(0, settings);
    }

    public LinearCongrentialPieceGenerator(long seed, PieceSettings settings) {
        this(seed, 1664525, 1013904223, settings);
    }

    public LinearCongrentialPieceGenerator(long seed, long a, long b, PieceSettings settings) {
        super(settings);
        this.seed = seed;
        this.a = a;
        this.b = b;
    }

    public LinearCongrentialPieceGenerator copy() {
        return new LinearCongrentialPieceGenerator(seed, settings);
    }

    @Override
    public int nextPieceNumber() {
        seed = (seed * a) & BIT_MASK;
        seed = (seed + b) & BIT_MASK;

        return modulus7() + 1;
    }

    private int modulus7() {
      long div = seed / 7;
      return (int)(seed - div * 7);
    }

    @Override
    public String export() {
        return "piece generator:" +
                "\n  description: Linear congruential generator" +
                "\n  class: " + this.getClass().getCanonicalName() +
                "\n  a: " + a +
                "\n  b: " + b +
                "\n  seed: " + seed + "\n";
    }

    @Override
    public String toString() {
        return export();
    }
}