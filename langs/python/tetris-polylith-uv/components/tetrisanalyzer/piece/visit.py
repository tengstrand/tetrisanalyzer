def is_visited(visited_moves, x, y, rotation):
    try:
        visited_rotations = visited_moves[y][x]
        return (visited_rotations & (1 << rotation)) != 0
    except IndexError:
        return True


def visit(visited_moves, x, y, rotation):
    visited_moves[y][x] |= (1 << rotation)
