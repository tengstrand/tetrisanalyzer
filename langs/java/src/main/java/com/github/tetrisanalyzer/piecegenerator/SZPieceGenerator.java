package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.settings.SettingsReader;

import java.util.Map;

public class SZPieceGenerator extends PieceGenerator {
    public long seed;

    public SZPieceGenerator(long seed) {
        this.seed = seed;
    }

    /**
     * Called via reflection.
     */
    public SZPieceGenerator(Map settings) {
        SettingsReader reader = new SettingsReader(settings, "piece generators");
        seed = reader.readLong("seed", 1);
    }

    @Override
    public PieceGenerator copy() {
        return new SZPieceGenerator(seed);
    }

    @Override
    public String export() {
        return  "{ seed: " + seed + " }";
    }

    @Override
    public int nextPieceNumber() {
        seed = (seed * 1664525) & 0x00000000FFFFFFFFL;
        seed = (seed + 1013904223) & 0x00000000FFFFFFFFL;

        return modulus2() + 3;
    }

    private int modulus2() {
        long div = seed / 2;
        return (int)(seed - div * 2);
    }
}
