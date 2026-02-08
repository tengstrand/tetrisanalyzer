def shape(piece_grid):
    cells = []
    for y, row in enumerate(piece_grid):
        for x, ch in enumerate(str(row)):
            if ch == "x":
                cells.append([x, y])
    return cells


def shapes(piece_grids):
    return [[shape(rotation_grid) for rotation_grid in rotations] for rotations in piece_grids]
