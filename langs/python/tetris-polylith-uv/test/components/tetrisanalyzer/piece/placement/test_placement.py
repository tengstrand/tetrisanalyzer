from tetrisanalyzer import piece


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
        piece.placement.placements(board, piece.J, start_x, False, piece.settings.atari_arcade.shapes),
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
        piece.placement.placements(board, piece.J, start_x, True, piece.settings.atari_arcade.shapes),
        key=sorter,
    )
