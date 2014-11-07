package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.settings.adjustment.Adjustments;

import java.util.Map;

import static com.github.tetrisanalyzer.settings.SettingsFunctions.rotation;
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
        this(board.width, sliding, false);
    }

    public AtariGameSettings(ColoredBoard board, boolean sliding) {
        this(board.width, sliding, false);
    }

    public AtariGameSettings() {
        this(10);
    }

    public AtariGameSettings(boolean sliding) {
        this(10, sliding, false);
    }

    public AtariGameSettings(int boardWidth) {
        this(boardWidth, false, false);
    }

    public AtariGameSettings(int boardWidth, Map settings) {
        this(boardWidth, SettingsFunctions.sliding(settings), rotation(settings));
    }

    public AtariGameSettings(int boardWidth, boolean sliding, boolean clockwise) {
        super("Atari", "http://www.arcade-museum.com/game_detail.php?game_id=10081",
                "Rules of the original Tetris arcade game", startX(boardWidth, 4), 0, sliding, clockwise, AtariGameSettings.class, adjustments());
    }

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
