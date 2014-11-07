package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.settings.SettingsFunctions;

import java.util.Map;

import static com.github.tetrisanalyzer.settings.Setting.setting;

/**
 * This piece generator mimics the behaviour of the C++ version
 * of Tetris Analyzer that uses 32 bit unsigned integers.
 */
public class LinearCongrentialPieceGenerator extends PieceGenerator {
    private static long BIT_MASK = 0x00000000FFFFFFFFL;

    public long seed;
    public final long constant1;
    public final long constant2;

    public LinearCongrentialPieceGenerator() {
        this(0);
    }

    public LinearCongrentialPieceGenerator(long seed) {
        this(seed, 1664525, 1013904223);
    }

    /**
     * Called via reflection.
     */
    public LinearCongrentialPieceGenerator(Map map) {
        this(seed(map), constant1(map), constant2(map));
    }

    public static long seed(Map settings) {
        SettingsFunctions.checkKey("seed", settings);
        return Long.parseLong(settings.get("seed").toString());
    }

    public static long constant1(Map settings) {
        if (!settings.containsKey("constant1")) {
            return 1664525;
        }
        return Long.parseLong(settings.get("constant1").toString());
    }

    public static long constant2(Map settings) {
        if (!settings.containsKey("constant2")) {
            return 1013904223;
        }
        return Long.parseLong(settings.get("constant2").toString());
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