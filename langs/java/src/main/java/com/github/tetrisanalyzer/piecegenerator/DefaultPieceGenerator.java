package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.settings.PieceSettings;

/**
 * This piece generator mimics the behaviour of the C++ version,
 * of TetrisAnalyzer that uses 32 bit unsigned integers.
 */
public class DefaultPieceGenerator extends PieceGenerator {
    private static long BIT_MASK = 0x00000000FFFFFFFFL;

    private long seed;

    public DefaultPieceGenerator(PieceSettings settings) {
        super(settings);
        this.seed = 0;
    }

    public DefaultPieceGenerator(long seed, PieceSettings settings) {
        super(settings);
        this.seed = seed;
    }

    public DefaultPieceGenerator copy() {
        return new DefaultPieceGenerator(seed, settings);
    }

    @Override
    public int nextPieceNumber() {
        seed = (seed * 1664525) & BIT_MASK;
        seed = (seed + 1013904223) & BIT_MASK;

        return modulus7() + 1;
    }

    private int modulus7() {
      long div = seed / 7;
      return (int)(seed - div * 7);
    }

    @Override
    public String export() {
        return "piece generator:" +
                "\n  description: Linear congruential generator (1664525,1013904223)" +
                "\n  class: " + this.getClass().getCanonicalName() +
                "\n  seed: " + seed + "\n";
    }

    @Override
    public String toString() {
        return export();
    }
}