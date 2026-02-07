def shape(piece_grid):
    """Convert a piece grid (strings with 'x' and '-') into a list of [x, y] cells.

    Mirrors `tetrisanalyzer.piece.shape/shape` in the Clojure implementation.

    Example:
        ["--x-",
         "-xx-",
         "-x--",
         "----"]

        -> [[2, 0], [1, 1], [2, 1], [1, 2]]
    """

    cells = []
    for y, row in enumerate(piece_grid):
        for x, ch in enumerate(str(row)):
            if ch == "x":
                cells.append([x, y])
    return cells


def shapes(piece_grids):
    """Convert a list of piece rotation grids into cell coordinate shapes."""

    return [[shape(rotation_grid) for rotation_grid in rotations] for rotations in piece_grids]
