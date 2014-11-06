package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.move.rotation.AnticlockwiseRotation;
import com.github.tetrisanalyzer.move.rotation.RotationDirection;
import com.github.tetrisanalyzer.settings.adjustment.Adjustments;

import static com.github.tetrisanalyzer.settings.StartPieceCalculator.startX;
import static com.github.tetrisanalyzer.settings.adjustment.AdjustmentCalculator.calculate;

/**
 * Settings of the Atari arcade game:
 *   http://www.arcade-museum.com/game_detail.php?game_id=10081
 */
public class AtariGameSettings extends GameSettings {

    public AtariGameSettings(Board board) {
        this(board.width);
    }

    public AtariGameSettings(ColoredBoard board) {
        this(board.width);
    }

    public AtariGameSettings(Board board, boolean sliding) {
        this(board.width, sliding);
    }

    public AtariGameSettings(ColoredBoard board, boolean sliding) {
        this(board.width, sliding);
    }

    public AtariGameSettings() {
        this(10);
    }

    public AtariGameSettings(boolean sliding) {
        this(10, sliding);
    }

    public AtariGameSettings(int boardWidth) {
        this(boardWidth, false);
    }

    public AtariGameSettings(int boardWidth, boolean sliding) {
        super(startX(boardWidth, 4), 0, sliding, AtariGameSettings.class, adjustments());
    }

    @Override public String id() { return "Atari"; }
    @Override public String url() { return "http://hem.bredband.net/joakimtengstrand"; }
    @Override public String description() { return "Tetris Analyzer 2 (Joakim Tengstrand)"; }
    @Override public RotationDirection rotationDirection() { return new AnticlockwiseRotation(); }

    public static Adjustments[] adjustments() {
        return new Adjustments[] {
                calculate("-", dxdy(0,0)),
                calculate("O", dxdy(0,0)),
                calculate("I", dxdy(0,1), dxdy(1,0)),
                calculate("S", dxdy(0,0), dxdy(0,0)),
                calculate("Z", dxdy(0,0), dxdy(0,0)),
                calculate("L", dxdy(0,0), dxdy(0,0), dxdy(0,0), dxdy(0,0)),
                calculate("J", dxdy(0,0), dxdy(0,0), dxdy(0,0), dxdy(0,0)),
                calculate("T", dxdy(0,0), dxdy(0,0), dxdy(0,0), dxdy(0,0)),
                calculate("x", dxdy(0,0)),
                calculate("+", dxdy(0,0))
        };
    }
}
