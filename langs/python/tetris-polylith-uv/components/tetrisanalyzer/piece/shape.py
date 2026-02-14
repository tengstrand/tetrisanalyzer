def shape(piece_grid):
    return [
        [x, y]
        for y, row in enumerate(piece_grid)
        for x, ch in enumerate(row)
        if ch == "x"]

def shapes(pieces_grids):
    return [
        [shape(piece_grid) for piece_grid in piece_grids]
        for piece_grids in pieces_grids]
