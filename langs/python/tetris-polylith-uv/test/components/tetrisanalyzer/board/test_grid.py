from tetrisanalyzer.board.grid import board, row


def test_grid_row_to_row():
    assert [0, 0, 1, 0, 0, 1, 1, 0] == row("--x--xx-")


def test_grid_to_board():
    assert [
        [0, 0, 1, 0, 0, 1, 1, 0],
        [1, 0, 0, 0, 0, 0, 0, 1],
    ] == board(
        [
            "--x--xx-",
            "x------x",
        ]
    )
