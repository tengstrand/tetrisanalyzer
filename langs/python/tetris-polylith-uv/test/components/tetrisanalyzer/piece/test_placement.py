from tetrisanalyzer import piece
from tetrisanalyzer.piece.settings import atari_arcade


def sorter(m):
    return (m[1], m[0], m[2])


def test_placements_without_rotation_kick():
    start_x = 2

    board = [
        [0, 0, 0, 0, 0, 0],
        [0, 0, 1, 1, 0, 0],
        [0, 0, 1, 0, 0, 1],
        [0, 0, 1, 1, 1, 1],
    ]

    assert [[2, 0, 0], [3, 0, 0]] == sorted(
        piece.placements(board, piece.J, start_x, False, atari_arcade.shapes),
        key=sorter,
    )


def test_placements_with_rotation_kick():
    start_x = 2

    board = [
        [0, 0, 0, 0, 0, 0],
        [0, 0, 1, 1, 0, 0],
        [0, 0, 1, 0, 0, 1],
        [0, 0, 1, 1, 1, 1],
    ]

    assert [[1, 0, 1], [2, 0, 0], [3, 0, 0], [0, 1, 1]] == sorted(
        piece.placements(board, piece.J, start_x, True, atari_arcade.shapes),
        key=sorter,
    )
