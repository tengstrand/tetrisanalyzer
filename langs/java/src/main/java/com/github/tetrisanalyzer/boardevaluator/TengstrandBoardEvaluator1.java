package com.github.tetrisanalyzer.boardevaluator;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.BoardOutline;
import com.github.tetrisanalyzer.piecemove.AllValidPieceMoves;
import com.github.tetrisanalyzer.settings.SettingsReader;

import java.util.Map;

import static com.github.tetrisanalyzer.settings.Setting.setting;

/**
 * Joakim Tengstrand's Tetris AI, version 1.2
 */
public class TengstrandBoardEvaluator1 extends BoardEvaluator {
    private int boardWidth;
    private int boardHeight;
    private double maxEquity;
    private double maxEquityFactor = 1.01;

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

    private double[] heightFactors;
    private double[] hollowFactors;
    private double[] areaWidthFactors;
    private double[] areaHeightFactors;
    private double[] areaHeightEqFactors;

    public TengstrandBoardEvaluator1() {
        this(10, 20);
    }

    public TengstrandBoardEvaluator1(Map settings) {
        this(boardWidth(settings), boardHeight(settings));

        SettingsReader reader = new SettingsReader(settings, "board evaluators");

        boardWidth = boardWidth(settings);
        boardHeight = boardHeight(settings);
        maxEquityFactor = reader.readDouble("maxEquityFactor");
        heightFactor0 = reader.readDouble("heightFactor0");
        heightFactor1 = reader.readDouble("heightFactor1");
        heightFactorDelta = reader.readDouble("heightFactorDelta");
        hollowFactor1 = reader.readDouble("hollowFactor1");
        hollowFactor2 = reader.readDouble("hollowFactor2");
        hollowFactorDelta = reader.readDouble("hollowFactorDelta");
        hollowFactorDeltaDelta = reader.readDouble("hollowFactorDeltaDelta");
        areaWidthFactor1 = reader.readDouble("areaWidthFactor1");
        areaWidthFactor2 = reader.readDouble("areaWidthFactor2");
        areaWidthFactor3 = reader.readDouble("areaWidthFactor3");
        areaWidthFactor4 = reader.readDouble("areaWidthFactor4");
        areaWidthFactor5 = reader.readDouble("areaWidthFactor5");
        areaWidthFactor6 = reader.readDouble("areaWidthFactor6");
        areaWidthFactor7 = reader.readDouble("areaWidthFactor7");
        areaWidthFactor8 = reader.readDouble("areaWidthFactor8");
        areaWidthFactor9 = reader.readDouble("areaWidthFactor9");
        areaHeightFactor1 = reader.readDouble("areaHeightFactor1");
        areaHeightEqFactor1 = reader.readDouble("areaHeightEqFactor1");
        areaHeightFactor2 = reader.readDouble("areaHeightFactor2");
        areaHeightEqFactor2 = reader.readDouble("areaHeightEqFactor2");
        areaHeightFactor3 = reader.readDouble("areaHeightFactor3");
        areaHeightEqFactor3 = reader.readDouble("areaHeightEqFactor3");
        areaHeightFactor4 = reader.readDouble("areaHeightFactor4");
        areaHeightFactor5 = reader.readDouble("areaHeightFactor5");
        areaHeightFactorDelta = reader.readDouble("areaHeightFactorDelta");
    }

    public TengstrandBoardEvaluator1(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        heightFactors = new double[boardHeight + 1];
        hollowFactors = new double[boardWidth];
        areaWidthFactors = new double[boardWidth < 10 ? 10 : boardWidth];
        areaHeightFactors = new double[boardHeight < 5 ? 5 : boardHeight + 1];
        areaHeightEqFactors = new double[boardHeight < 5 ? 5 : boardHeight + 1];

        maxEquity = boardWidth * (boardHeight - 1) * Math.pow(maxEquityFactor, (boardHeight - 1));

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

    public TengstrandBoardEvaluator1 copy() {
        return new TengstrandBoardEvaluator1(
                boardWidth, boardHeight, maxEquity, maxEquityFactor,
                heightFactor0, heightFactor1, heightFactorDelta,
                hollowFactor1, hollowFactor2, hollowFactorDelta, hollowFactorDeltaDelta,
                areaWidthFactor1, areaWidthFactor2, areaWidthFactor3,
                areaWidthFactor4, areaWidthFactor5, areaWidthFactor6,
                areaWidthFactor7, areaWidthFactor8, areaWidthFactor9,
                areaHeightFactor1, areaHeightFactor2, areaHeightFactor3,
                areaHeightFactor4, areaHeightFactor5, areaHeightEqFactor1,
                areaHeightEqFactor2, areaHeightEqFactor3, areaHeightFactorDelta);
    }

    private TengstrandBoardEvaluator1(int boardWidth, int boardHeight, double maxEquity, double maxEquityFactor,
                                     double heightFactor0, double heightFactor1, double heightFactorDelta,
                                     double hollowFactor1, double hollowFactor2, double hollowFactorDelta, double hollowFactorDeltaDelta,
                                     double areaWidthFactor1, double areaWidthFactor2, double areaWidthFactor3,
                                     double areaWidthFactor4, double areaWidthFactor5, double areaWidthFactor6,
                                     double areaWidthFactor7, double areaWidthFactor8, double areaWidthFactor9,
                                     double areaHeightFactor1, double areaHeightFactor2, double areaHeightFactor3,
                                     double areaHeightFactor4, double areaHeightFactor5, double areaHeightEqFactor1,
                                     double areaHeightEqFactor2, double areaHeightEqFactor3, double areaHeightFactorDelta) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.maxEquity = maxEquity;
        this.maxEquityFactor = maxEquityFactor;
        this.heightFactor0 = heightFactor0;
        this.heightFactor1 = heightFactor1;
        this.heightFactorDelta = heightFactorDelta;
        this.hollowFactor1 = hollowFactor1;
        this.hollowFactor2 = hollowFactor2;
        this.hollowFactorDelta = hollowFactorDelta;
        this.hollowFactorDeltaDelta = hollowFactorDeltaDelta;
        this.areaWidthFactor1 = areaWidthFactor1;
        this.areaWidthFactor2 = areaWidthFactor2;
        this.areaWidthFactor3 = areaWidthFactor3;
        this.areaWidthFactor4 = areaWidthFactor4;
        this.areaWidthFactor5 = areaWidthFactor5;
        this.areaWidthFactor6 = areaWidthFactor6;
        this.areaWidthFactor7 = areaWidthFactor7;
        this.areaWidthFactor8 = areaWidthFactor8;
        this.areaWidthFactor9 = areaWidthFactor9;
        this.areaHeightFactor1 = areaHeightFactor1;
        this.areaHeightFactor2 = areaHeightFactor2;
        this.areaHeightFactor3 = areaHeightFactor3;
        this.areaHeightFactor4 = areaHeightFactor4;
        this.areaHeightFactor5 = areaHeightFactor5;
        this.areaHeightEqFactor1 = areaHeightEqFactor1;
        this.areaHeightEqFactor2 = areaHeightEqFactor2;
        this.areaHeightEqFactor3 = areaHeightEqFactor3;
        this.areaHeightFactorDelta = areaHeightFactorDelta;
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

    @Override public String id() { return "Tengstrand 1.2"; }
    @Override public String description() { return "A first version was created 2001"; }
    @Override public String author() { return "Joakim Tengstrand"; }
    @Override public String url() { return "http://hem.bredband.net/joakimtengstrand"; }
    @Override public int boardWidth() { return boardWidth; }
    @Override public int boardHeight() { return boardHeight; }
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
