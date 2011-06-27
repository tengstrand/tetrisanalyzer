package nu.tengstrand.tetrisanalyzer.piecegenerator;

/**
 * This piece generator mimics the behaviour of the C++ version,
 * that uses 32 bit unsigned integers.
 */
public class DefaultPieceGenerator extends PieceGenerator {
    private static long BIT_MASK = 0x00000000FFFFFFFFL;

    private long seed = 0;

    public DefaultPieceGenerator() {
        this.seed = seed;
    }

    public DefaultPieceGenerator(long seed) {
        this.seed = seed;
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
}