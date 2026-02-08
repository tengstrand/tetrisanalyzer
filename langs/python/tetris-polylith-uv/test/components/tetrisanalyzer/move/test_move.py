from tetrisanalyzer import piece
from tetrisanalyzer.board import board
from tetrisanalyzer.move.move import (
    down,
    is_valid_move,
    left,
    right,
    rotate,
    rotate_with_kick,
)
from tetrisanalyzer.piece.settings import atari_arcade

BOARD = board(
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
BITMASK = piece.rotation_bitmask(SHAPES, S)


def test_valid_move():
    assert is_valid_move(BOARD, X, Y, S, ROTATION, SHAPES) is True


def test_valid_left_move():
    assert left(BOARD, X + 1, Y, S, ROTATION, None, SHAPES) == [2, 1, 0]


def test_invalid_left_move():
    assert left(BOARD, X, Y, S, ROTATION, None, SHAPES) is None


def test_valid_right_move():
    assert right(BOARD, X - 1, Y, S, ROTATION, None, SHAPES) == [2, 1, 0]


def test_invalid_right_move():
    assert right(BOARD, X, Y - 1, S, ROTATION, None, SHAPES) is None


def test_unoccupied_down_move():
    assert down(BOARD, X, Y - 1, S, ROTATION, None, SHAPES) == ([2, 1, 0], None)


def test_down_move_hits_ground():
    assert down(BOARD, X, Y, S, ROTATION, None, SHAPES) == (None, [[2, 1, 0]])


def test_valid_rotation():
    assert rotate(BOARD, X, Y, S, ROTATION - 1, BITMASK, SHAPES) == [2, 1, 0]


def test_invalid_rotation_without_kick():
    assert rotate(BOARD, X + 1, Y, S, ROTATION + 1, BITMASK, SHAPES) is None


def test_valid_rotation_with_kick():
    assert rotate_with_kick(BOARD, X + 1, Y, S, ROTATION + 1, BITMASK, SHAPES) == [2, 1, 0]


def test_invalid_move_outside_board():
    assert is_valid_move(BOARD, 10, -10, S, ROTATION, SHAPES) is False
