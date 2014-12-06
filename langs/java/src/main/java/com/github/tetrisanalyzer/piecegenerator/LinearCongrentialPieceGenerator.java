package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.settings.SettingsReader;

import java.util.Map;

/**
 * This piece generator mimics the behaviour of the C++ version
 * of Tetris Analyzer that uses 32 bit unsigned integers.
 */
public class LinearCongrentialPieceGenerator extends PieceGenerator {
    private static long BIT_MASK = 0x00000000FFFFFFFFL;

    private static final long CONSTANT1 = 1664525;
    private static final long CONSTANT2 = 1013904223;

    public long seed;
    public long constant1;
    public long constant2;

    public LinearCongrentialPieceGenerator(long seed) {
        this(seed, CONSTANT1, CONSTANT2);
    }

    public LinearCongrentialPieceGenerator(long seed, long constant1, long constant2) {
        this.seed = seed;
        this.constant1 = constant1;
        this.constant2 = constant2;
    }

    /**
     * Called via reflection.
     */
    public LinearCongrentialPieceGenerator(Map settings) {
        SettingsReader reader = new SettingsReader(settings, "piece generators");

        seed = reader.readLong("seed", 1);
        constant1 = reader.readLong("constant1", CONSTANT1);
        constant2 = reader.readLong("constant2", CONSTANT2);
    }

    public LinearCongrentialPieceGenerator copy() {
        return new LinearCongrentialPieceGenerator(seed);
    }

    @Override
    public String export() {
        String result = "{ seed: " + seed;
        if (constant1 != CONSTANT1) {
            result += ", constant1: " + constant1;
        }
        if (constant2 != CONSTANT2) {
            result += ", constant2: " + constant2;
        }
        return result + " }";
    }

    @Override
    public int nextPieceNumber() {
        seed = (seed * constant1) & BIT_MASK;
        seed = (seed + constant2) & BIT_MASK;

        return modulus7() + 1;
    }

    private int modulus7() {
      long div = seed / 7;
      return (int)(seed - div * 7);
    }

    @Override
    public String toString() {
        return export();
    }
}