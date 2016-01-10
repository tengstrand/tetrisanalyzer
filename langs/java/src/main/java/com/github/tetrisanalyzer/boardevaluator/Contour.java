package com.github.tetrisanalyzer.boardevaluator;

import com.github.tetrisanalyzer.board.BoardOutline;

public class Contour {
    private final int[] from;
    private final int[] to;

    public static Contour Z0 = new Contour(new int[] { -999,0,1,1,-1 }, new int[] { 0,0,1,1,1 });
    public static Contour Z1 = new Contour(new int[] { -999,0,-1,-999 }, new int[] { -1,0,-1,-2 });
    public static Contour S0 = new Contour(new int[] { -999,0,0,-1,-999 }, new int[] { 0,0,0,-1,-1 });
    public static Contour S1 = new Contour(new int[] { -999,0,1,-999 }, new int[] { -1,0,1,0 });

    public Contour(int[] from, int[] to) {
        this.from = from;
        this.to = to;
    }

    public boolean fit(BoardOutline outline) {

        outer:
        for (int cx = 0; cx<=outline.size() - from.length; cx++) {
            int y0 = outline.get(cx + 1);

            for (int x=0; x<from.length; x++) {
                int y = outline.get(cx + x) - y0;
                if (y < from[x] || y > to[x]) {
                    continue outer;
                }
            }
            return true;
        }
        return false;
    }

    public static double evaluate(BoardOutline outline, int boardWidth, double penalty) {
        double s = S0.fit(outline) || S1.fit(outline) ? 0 : penalty;
        double z = Z0.fit(outline) || Z1.fit(outline) ? 0 : penalty;

        return (s + z) / boardWidth;
    }
}
