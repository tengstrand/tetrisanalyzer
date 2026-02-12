from tetrisanalyzer import board, piece
from tetrisanalyzer.piece.bitmask import rotation_bitmask
from tetrisanalyzer.piece.move import (
    is_valid_move,
    left,
    right,
    down,
    rotate,
    rotate_with_kick,
)
from tetrisanalyzer.piece.settings import atari_arcade

BOARD = board.board(
    [
        "xxxxxxxx",
        "xxx--xxx",
        "xx--xxxx",
        "xxxxxxxx",
    ]
)

X = 2
Y = 1
ROTATION = 0
S = piece.S
SHAPES = atari_arcade.shapes
BITMASK = rotation_bitmask(SHAPES, S)


def test_valid_move():
    assert is_valid_move(BOARD, X, Y, S, ROTATION, SHAPES) is True


def test_valid_left_move():
    assert [2, 1, 0] == left(BOARD, X + 1, Y, S, ROTATION, None, SHAPES)


def test_invalid_left_move():
    assert left(BOARD, X, Y, S, ROTATION, None, SHAPES) is None


def test_valid_right_move():
    assert [2, 1, 0] == right(BOARD, X - 1, Y, S, ROTATION, None, SHAPES)


def test_invalid_right_move():
    assert right(BOARD, X, Y - 1, S, ROTATION, None, SHAPES) is None


def test_unoccupied_down_move():
    assert ([2, 1, 0], None) == down(BOARD, X, Y - 1, S, ROTATION, None, SHAPES)


def test_down_move_hits_ground():
    assert (None, [[2, 1, 0]]) == down(BOARD, X, Y, S, ROTATION, None, SHAPES)


def test_valid_rotation():
    assert [2, 1, 0] == rotate(BOARD, X, Y, S, ROTATION - 1, BITMASK, SHAPES)


def test_invalid_rotation_without_kick():
    assert rotate(BOARD, X + 1, Y, S, ROTATION + 1, BITMASK, SHAPES) is None


def test_valid_rotation_with_kick():
    assert [2, 1, 0] == rotate_with_kick(BOARD, X + 1, Y, S, ROTATION + 1, BITMASK, SHAPES)


def test_invalid_move_outside_board():
    assert is_valid_move(BOARD, 10, -10, S, ROTATION, SHAPES) is False
