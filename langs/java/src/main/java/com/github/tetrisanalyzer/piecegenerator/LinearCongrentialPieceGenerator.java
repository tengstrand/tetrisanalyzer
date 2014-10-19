package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.settings.PieceSettings;

import static com.github.tetrisanalyzer.settings.Setting.setting;

/**
 * This piece generator mimics the behaviour of the C++ version
 * of Tetris Analyzer that uses 32 bit unsigned integers.
 */
public class LinearCongrentialPieceGenerator extends PieceGenerator {
    private static long BIT_MASK = 0x00000000FFFFFFFFL;

    private long seed;
    private final long constant1;
    private final long constant2;

    public LinearCongrentialPieceGenerator(PieceSettings settings) {
        this(0, settings);
    }

    public LinearCongrentialPieceGenerator(long seed, PieceSettings settings) {
        this(seed, 1664525, 1013904223, settings);
    }

    public LinearCongrentialPieceGenerator(long seed, long constant1, long constant2, PieceSettings settings) {
        super(settings);
        this.seed = seed;
        this.constant1 = constant1;
        this.constant2 = constant2;
    }

    public LinearCongrentialPieceGenerator copy() {
        return new LinearCongrentialPieceGenerator(seed, settings);
    }

    @Override
    public String description() {
        return "Linear congruential generator";
    }

    @Override
    public PieceGeneratorSettings settings() {
        return new PieceGeneratorSettings(
                setting("constant 1", constant1),
                setting("constant 2", constant2),
                setting("seed", seed));
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