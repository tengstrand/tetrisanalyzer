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
public class TengstrandBoardEvaluator12 extends BoardEvaluator {
    public int boardWidth;
    public int boardHeight;
    public double maxEquity;
    public double maxEquityFactor = 1.21;

    public double heightFactor0 = 7;
    public double heightFactor1 = 2.5;
    public double heightFactorDelta = 0.81;

    public double hollowFactor1 = 0.533;
    public double hollowFactor2 = 0.6;
    public double hollowFactorDelta = 0.85;

    public double areaWidthFactor1 = 4.97;
    public double areaWidthFactor2 = 2.38;
    public double areaWidthFactor3 = 3.0;
    public double areaWidthFactor4 = 2.21;
    public double areaWidthFactor5 = 2.05;
    public double areaWidthFactor6 = 1.87;
    public double areaWidthFactor7 = 1.52;
    public double areaWidthFactor8 = 1.34;
    public double areaWidthFactor9 = 1.18;

    public double areaHeightFactor1 = 0.5;
    public double areaHeightFactor2 = 1.19;
    public double areaHeightFactor3 = 2.35;
    public double areaHeightFactor4 = 3.1;
    public double areaHeightFactor5 = 4.6;
    public double areaHeightFactorDelta = 1;

    public double areaHeightEqFactor1 = 0.42;
    public double areaHeightEqFactor2 = 1.05;
    public double areaHeightEqFactor3 = 2.2;

    public double[] heightFactors;
    public double[] hollowFactors;
    public double[] areaWidthFactors;
    public double[] areaHeightFactors;
    public double[] areaHeightEqFactors;

    public TengstrandBoardEvaluator12(int boardWidth, int boardHeight) {
        init(boardWidth, boardHeight);
    }

    public TengstrandBoardEvaluator12(int boardWidth, int boardHeight, Map settings) {
        SettingsReader reader = new SettingsReader(settings, "board evaluators");

        maxEquityFactor = reader.readDouble("maxEquityFactor");
        heightFactor0 = reader.readDouble("heightFactor0");
        heightFactor1 = reader.readDouble("heightFactor1");
        heightFactorDelta = reader.readDouble("heightFactorDelta");
        hollowFactor1 = reader.readDouble("hollowFactor1");
        hollowFactor2 = reader.readDouble("hollowFactor2");
        hollowFactorDelta = reader.readDouble("hollowFactorDelta");
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

        init(boardWidth, boardHeight);
    }

    private void init(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        heightFactors = new double[boardHeight + 1];
        hollowFactors = new double[boardWidth];
        areaWidthFactors = new double[boardWidth < 10 ? 10 : boardWidth];
        areaHeightFactors = new double[boardHeight < 5 ? 5 : boardHeight + 1];
        areaHeightEqFactors = new double[boardHeight < 5 ? 5 : boardHeight + 1];

        maxEquity = boardWidth * (boardHeight - 1) * maxEquityFactor;

        initHeightFactors();
        initHollowFactors();
        initAreaWidthFactors();
        initAreaHeightFactors();
        initAreaHeightEqFactors();
    }

    private void initHeightFactors() {
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

        for (int i=3; i<hollowFactors.length; i++) {
            factor *= hollowFactorDelta;
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
