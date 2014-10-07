package com.github.tetrisanalyzer.move;

import com.github.tetrisanalyzer.piecemove.PieceMove;

public class MoveEquity {
    public PieceMove pieceMove;
    public double equity;

    public MoveEquity(PieceMove pieceMove, double equity) {
        this.pieceMove = pieceMove;
        this.equity = equity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoveEquity that = (MoveEquity) o;

        if (Double.compare(that.equity, equity) != 0) return false;
        if (pieceMove != null ? !pieceMove.equals(that.pieceMove) : that.pieceMove != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = pieceMove != null ? pieceMove.hashCode() : 0;
        temp = equity != +0.0d ? Double.doubleToLongBits(equity) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "MoveEquity{" +
                "pieceMove=" + pieceMove +
                ", equity=" + equity +
                '}';
    }
}
