def row(grid_row):
    return [0 if c == "-" else 1 for c in str(grid_row)]


def board(grid_board):
    return [row(r) for r in grid_board]
