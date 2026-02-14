from tetrisanalyzer.piece import core as piece


def cell(board, x, y, cx, cy):
    yy = y + cy
    xx = x + cx
    try:
        if yy < 0 or xx < 0:
            return piece.X
        return board[yy][xx]
    except IndexError:
        return piece.X


def is_valid_move(board, x, y, p, rotation, shapes):
    return all(cell(board, x, y, *c) == 0 for c in piece.piece(p, rotation, shapes))


def left(board, x, y, p, rotation, _bitmask, shapes):
    return [x - 1, y, rotation] if is_valid_move(board, x - 1, y, p, rotation, shapes) else None


def right(board, x, y, p, rotation, _bitmask, shapes):
    return [x + 1, y, rotation] if is_valid_move(board, x + 1, y, p, rotation, shapes) else None


def down(board, x, y, p, rotation, _bitmask, shapes):
    """Return (down_move, placement).

    - down_move: next move when moving down, or None if blocked
    - placement: [[x, y, rotation]] if blocked, otherwise None
    """

    if is_valid_move(board, x, y + 1, p, rotation, shapes):
        return [x, y + 1, rotation], None
    return None, [[x, y, rotation]]


def rotate(board, x, y, p, rotation, bitmask, shapes):
    new_rotation = (rotation + 1) & bitmask
    return [x, y, new_rotation] if is_valid_move(board, x, y, p, new_rotation, shapes) else None


def rotate_with_kick(board, x, y, p, rotation, bitmask, shapes):
    return (
        rotate(board, x, y, p, rotation, bitmask, shapes) or
        rotate(board, x - 1, y, p, rotation, bitmask, shapes)
    )


def rotation_fn(rotation_kick):
    return rotate_with_kick if rotation_kick else rotate
