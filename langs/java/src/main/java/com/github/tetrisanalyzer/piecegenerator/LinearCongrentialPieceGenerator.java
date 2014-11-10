package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.settings.SettingsReader;

import java.util.Map;

import static com.github.tetrisanalyzer.settings.Setting.setting;

/**
 * This piece generator mimics the behaviour of the C++ version
 * of Tetris Analyzer that uses 32 bit unsigned integers.
 */
public class LinearCongrentialPieceGenerator extends PieceGenerator {
    private static long BIT_MASK = 0x00000000FFFFFFFFL;

    public long seed;
    public long constant1;
    public long constant2;

    public LinearCongrentialPieceGenerator() {
        this(0);
    }

    public LinearCongrentialPieceGenerator(long seed) {
        this(seed, 1664525, 1013904223);
    }

    /**
     * Called via reflection.
     */
    public LinearCongrentialPieceGenerator(Map settings) {
        super(readString(settings, "id"), readString(settings, "description"));

        SettingsReader reader = new SettingsReader(settings, "piece generators");
        seed = reader.readLong("seed");
        constant1 = reader.readLong("constant1", 1664525);
        constant2 = reader.readLong("constant2", 1013904223);
    }

    private static String readString(Map settings, String key) {
        return new SettingsReader(settings, "piece generators").readString(key);
    }

    public LinearCongrentialPieceGenerator(long seed, long constant1, long constant2) {
        super("lenear", "Linear congruential piece generator");
        this.seed = seed;
        this.constant1 = constant1;
        this.constant2 = constant2;
    }

    public LinearCongrentialPieceGenerator copy() {
        return new LinearCongrentialPieceGenerator(seed);
    }

    @Override
    public PieceGeneratorSettings settings() {
        return new PieceGeneratorSettings(
                setting("constant1", constant1),
                setting("constant2", constant2),
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