from tetrisanalyzer.board.grid import board, row


def test_grid_row_to_row():
    assert row("--x--xx-") == [0, 0, 1, 0, 0, 1, 1, 0]


def test_grid_to_board():
    assert board(
        [
            "--x--xx-",
            "x------x",
        ]
    ) == [
        [0, 0, 1, 0, 0, 1, 1, 0],
        [1, 0, 0, 0, 0, 0, 0, 1],
    ]
