package com.github.tetrisanalyzer.boardevaluator;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.BoardOutline;
import com.github.tetrisanalyzer.piecemove.AllValidPieceMoves;

import java.util.List;
import java.util.Map;

import static com.github.tetrisanalyzer.settings.Setting.setting;

/**
 * Joakim Tengstrand's Tetris AI, version 1.2
 */
public class TengstrandBoardEvaluator1 extends BoardEvaluator {
    private final int boardWidth;
    private final int boardHeight;
    private final double maxEquity;
    private final double maxEquityFactor = 1.204;

    private double heightFactor0 = 7;
    private double heightFactor1 = 2.5;
    private double heightFactorDelta = 0.86;

    private double hollowFactor1 = 0.533;
    private double hollowFactor2 = 0.6;
    private double hollowFactorDelta = 0.85;
    private double hollowFactorDeltaDelta = 0.95;

    private double areaWidthFactor1 = 4.95;
    private double areaWidthFactor2 = 2.39;
    private double areaWidthFactor3 = 3.1;
    private double areaWidthFactor4 = 2.21;
    private double areaWidthFactor5 = 2.05;
    private double areaWidthFactor6 = 1.87;
    private double areaWidthFactor7 = 1.52;
    private double areaWidthFactor8 = 1.34;
    private double areaWidthFactor9 = 1.18;

    private double areaHeightFactor1 = 0.5;
    private double areaHeightFactor2 = 1.19;
    private double areaHeightFactor3 = 2.3;
    private double areaHeightFactor4 = 3.1;
    private double areaHeightFactor5 = 4.6;
    private double areaHeightFactorDelta = 1;

    private double areaHeightEqFactor1 = 0.42;
    private double areaHeightEqFactor2 = 1.05;
    private double areaHeightEqFactor3 = 2.2;

    private double[] heightFactors = new double[21];
    private double[] hollowFactors = new double[10];
    private double[] areaWidthFactors = new double[10];
    private double[] areaHeightFactors = new double[21];
    private double[] areaHeightEqFactors = new double[21];

    public TengstrandBoardEvaluator1() {
        this(10, 20);
    }

/*
    public TengstrandBoardEvaluator1(Map settings) {

    }
*/
    public TengstrandBoardEvaluator1(int boardWidth, int boardHeight) {
        if (boardWidth > 10) {
            throw new IllegalArgumentException("Only board widths between 4 and 10 is supported at the moment, but was: " + boardWidth);
        }
        if (boardHeight > 20) {
            throw new IllegalArgumentException("Only board heights between 4 and 20 is supported at the moment, but was: " + boardHeight);
        }
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        maxEquity = (boardHeight - 2) * boardWidth * maxEquityFactor;

        initHeightFactor();
        initHollowFactors();
        initAreaWidthFactors();
        initAreaHeightFactors();
        initAreaHeightEqFactors();
    }

    private void initHeightFactor() {
        heightFactors[0] = heightFactor0;
        heightFactors[1] = heightFactor1;
        double factor = heightFactor1;

        for (int i=2; i< heightFactors.length; i++) {
            factor *= heightFactorDelta;
            heightFactors[i] = factor;
        }
    }

    private void initHollowFactors() {
        hollowFactors[1] = hollowFactor1;
        hollowFactors[2] = hollowFactor2;
        double factor = hollowFactor2;
        double delta = 1 - hollowFactorDelta;

        for (int i=3; i<hollowFactors.length; i++) {
            factor *= (1 - delta);
            delta *= hollowFactorDeltaDelta;
            hollowFactors[i] = factor;
        }
    }

    private void initAreaWidthFactors() {
        areaWidthFactors[1] = areaWidthFactor1;
        areaWidthFactors[2] = areaWidthFactor2;
        areaWidthFactors[3] = areaWidthFactor3;
        areaWidthFactors[4] = areaWidthFactor4;
        areaWidthFactors[5] = areaWidthFactor5;
        areaWidthFactors[6] = areaWidthFactor6;
        areaWidthFactors[7] = areaWidthFactor7;
        areaWidthFactors[8] = areaWidthFactor8;
        areaWidthFactors[9] = areaWidthFactor9;
        double factor = areaWidthFactor9;
        double delta = areaWidthFactor8 - areaWidthFactor9;

        for (int i=10; i<areaWidthFactors.length; i++) {
            factor -= delta;
            areaWidthFactors[i] = factor;
        }
    }

    private void initAreaHeightFactors() {
        areaHeightFactors[1] = areaHeightFactor1;
        areaHeightFactors[2] = areaHeightFactor2;
        areaHeightFactors[3] = areaHeightFactor3;
        areaHeightFactors[4] = areaHeightFactor4;
        areaHeightFactors[5] = areaHeightFactor5;

        double factor = areaHeightFactor5;

        for (int i=6; i<areaHeightFactors.length; i++) {
            factor += areaHeightFactorDelta;
            areaHeightFactors[i] = factor;
        }
    }

    private void initAreaHeightEqFactors() {
        areaHeightEqFactors[1] = areaHeightEqFactor1;
        areaHeightEqFactors[2] = areaHeightEqFactor2;
        areaHeightEqFactors[3] = areaHeightEqFactor3;
        areaHeightEqFactors[4] = areaHeightFactor4;
        areaHeightEqFactors[5] = areaHeightFactor5;

        double factor = areaHeightFactor5;

        for (int i=6; i<areaHeightEqFactors.length; i++) {
            factor += areaHeightFactorDelta;
            areaHeightEqFactors[i] = factor;
        }
    }

    public TengstrandBoardEvaluator1(int boardWidth, int boardHeight, Map<String,Number> parameters) {
        this(boardWidth, boardHeight);

        List heightfactor = (List)parameters.get("height factor");
        List hollowfactor = (List)parameters.get("hollow factor");
        List areawidthfactor = (List)parameters.get("area width factor");
        List areaheightfactor = (List)parameters.get("area height factor");
        List areaheightfactor2 = (List)parameters.get("area height factor2");

        if (parameters.containsKey("height factor")) {
            heightfactor = (List)parameters.get("height factor");
            if (!(heightfactor.size() == heightFactors.length)) throw new IllegalArgumentException("Expected " + heightFactors.length + " elements in 'height factor'");
            populatet(heightfactor, heightFactors);
        }
        if (parameters.containsKey("hollow factor")) {
            hollowfactor = (List)parameters.get("hollow factor");
            if (!(hollowfactor.size() == hollowFactors.length)) throw new IllegalArgumentException("Expected " + hollowFactors.length + " elements in 'hollow factor'");
            populatet(hollowfactor, hollowFactors);
        }
        if (parameters.containsKey("area width factor")) {
            areawidthfactor = (List)parameters.get("area width factor");
            if (!(areawidthfactor.size() == areaWidthFactors.length)) throw new IllegalArgumentException("Expected " + areaWidthFactors.length + " elements in 'area width factor'");
            populatet(areawidthfactor, areaWidthFactors);
        }
        if (parameters.containsKey("area height factor")) {
            areaheightfactor = (List)parameters.get("area height factor");
            if (!(areaheightfactor.size() == areaHeightFactors.length)) throw new IllegalArgumentException("Expected " + areaHeightFactors.length + " elements in 'area height factor'");
            populatet(areaheightfactor, areaHeightFactors);
        }
        if (parameters.containsKey("area height factor2")) {
            areaheightfactor2 = (List)parameters.get("area height factor2");
            if (!(areaheightfactor2.size() == areaHeightEqFactors.length)) throw new IllegalArgumentException("Expected " + areaHeightEqFactors.length + " elements in 'area height factor2'");
            populatet(areaheightfactor2, areaHeightEqFactors);
        }
        for (Map.Entry<String,Number> parameter : parameters.entrySet()) {
            setValue(parameter, "height factor", heightFactors);
            setValue(parameter, "hollow factor", hollowFactors);
            setValue(parameter, "area width factor", areaWidthFactors);
            setValue(parameter, "area height factor", areaHeightFactors);
            setValue(parameter, "area height factor2", areaHeightEqFactors);
        }
    }

    private void setValue(Map.Entry<String,Number> entry, String key, double[] array) {
        if (entry.getKey().startsWith(key + "[")) {
            String squaredIndex = entry.getKey().substring(key.length());
            if (!squaredIndex.startsWith("[") || !squaredIndex.endsWith("]")) {
                throw new IllegalArgumentException("Expected a value enclosed with [], but was: " + squaredIndex);
            }
            int index = Integer.valueOf(squaredIndex.substring(1, squaredIndex.length() - 1));
            if (!(entry.getValue() instanceof Number)) {
                throw new IllegalArgumentException("Expected a value, but was: " + entry.getValue());
            }
            if (index < 0 || index >= array.length) {
                throw new IllegalArgumentException("Index was outside valid range 0-" + array.length + ", but was: " + array.length);
            }
            array[index] = ((Number)entry.getValue()).doubleValue();
        }
    }

    private static void populatet(List source, double[] target) {
        int i = 0;
        for (Object value : source) {
            if (!(value instanceof Number)) {
                throw new IllegalArgumentException("Expected a number, but was a: " + value.getClass().getSimpleName() + "(" + value + ")");
            }
            target[i++] = ((Number) value).doubleValue();
        }
    }

    @Override
    public double evaluate(Board board, AllValidPieceMoves allValidPieceMoves)  {
        return allValidPieceMoves.adjustEquityIfOccupiedStartPiece(evaluate(board), maxEquity, board);
    }

    public double evaluate(Board board)  {
        if (board.width > boardWidth) {
            throw new IllegalArgumentException("Can not evaluate board width > " + boardWidth);
        }
        BoardOutline outline = new BoardOutline(board);

        return evaluateBasedOnHollows(board, outline) +
                evaluateBasedOnOutlineHeight(outline) +
                evaluateBasedOnOutlineStructure(outline);
    }

    private double evaluateBasedOnHollows(Board board, BoardOutline outline) {
        double equity = 0;
        double[] hollowFactorForRow = new double[boardHeight + 1];

        for (int y=outline.minY; y<boardHeight; y++) {
            int numberOfEmptySquaresPerRow = 0;
            int minOutlineForHole = boardHeight;

            for (int x=0; x<boardWidth; x++) {
                if (board.isFree(x, y)) {
                    numberOfEmptySquaresPerRow++;
                    if (outline.get(x) < minOutlineForHole && outline.get(x) < y) {
                        minOutlineForHole = outline.get(x);
                    }
                }
            }
            hollowFactorForRow[y] = hollowFactors[numberOfEmptySquaresPerRow];

            if (minOutlineForHole < boardHeight) {
                double hollowFactor = 1;

                for (int row=minOutlineForHole; row<=y; row++) {
                    hollowFactor *= hollowFactorForRow[row];
                }
                equity += (1 - hollowFactor) * boardWidth;
            }
        }
        return equity;
    }

    private double evaluateBasedOnOutlineHeight(BoardOutline outline) {
        double sum = 0;

        for (int x=0; x<boardWidth; x++) {
            sum += heightFactors[outline.get(x)];
        }
        return sum;
    }

    private double evaluateBasedOnOutlineStructure(BoardOutline outline) {
        double equity = 0;

        for (int x=1; x<=boardWidth; x++) {
            boolean hasAreaWallsSameHeight = false;
            boolean isAreaWallsSameHeightNotInitialized = true;
            int areaHeight = 0;
            int previousAreaWidth = 0;

            int rightWallY = outline.get(x);
            int startY = (x == boardWidth) ? outline.minY : outline.get(x);

            // Calculate the size of the closed area in the outline (areaWidth * areaHeight).
            for (int y=startY; y<=boardHeight; y++) {
                int areaWidth = 0;

                for (int areaX=x-1; areaX>=0; areaX--) {
                    if (outline.get(areaX) <= y) {
                        if (isAreaWallsSameHeightNotInitialized) {
                            hasAreaWallsSameHeight = outline.get(areaX) == rightWallY;
                            isAreaWallsSameHeightNotInitialized = false;
                        }
                        break;
                    }
                    areaWidth++;
                }
                if (areaWidth == 0 && previousAreaWidth == 0) {
                    break;
                } else {
                    if (areaWidth > 0 && (areaWidth == previousAreaWidth || previousAreaWidth == 0)) {
                        areaHeight++;
                    } else {
                        if (hasAreaWallsSameHeight) {
                            equity += areaWidthFactors[previousAreaWidth] * areaHeightEqFactors[areaHeight];
                        } else {
                            equity += areaWidthFactors[previousAreaWidth] * areaHeightFactors[areaHeight];
                        }
                        areaHeight = 1;
                        isAreaWallsSameHeightNotInitialized = true;
                    }
                    previousAreaWidth = areaWidth;
                }
            }
        }
        return equity;
    }

    @Override public String id() { return "Tengstrand 1.1"; }
    @Override public String description() { return "A first version was created 2001"; }
    @Override public String author() { return "Joakim Tengstrand"; }
    @Override public String url() { return "http://hem.bredband.net/joakimtengstrand"; }
    @Override public int minBoardX() { return 4; }
    @Override public int maxBoardX() { return 10; }
    @Override public int minBoardY() { return 4; }
    @Override public int maxBoardY() { return 20; }
    @Override public LessIs lessIs() { return LessIs.BETTER; }

    @Override
    public BoardEvaluatorSettings settings() {
        return new BoardEvaluatorSettings(
                setting("height factor", asList(heightFactors)),
                setting("hollow factor", asList(hollowFactors)),
                setting("area width factor", asList(areaWidthFactors)),
                setting("area height factor", asList(areaHeightFactors)),
                setting("area height factor2", asList(areaHeightEqFactors)));
    }

    private String asList(double[] array) {
        String result = "[";
        String separator = "";

        for (double value : array) {
            result += separator + value;
            separator = ", ";
        }
        return result + "]";
    }
}
