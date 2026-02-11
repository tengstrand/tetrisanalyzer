def width(board):
    return len(board[0])


def height(board):
    return len(board)


def empty_row(w):
    return [0] * w


def empty_board(width, height):
    w = int(width)
    h = int(height)
    return [empty_row(w) for _ in range(h)]


def empty_board_as(board):
    return empty_board(width(board), height(board))


def set_cell(board, p, x, y, cell):
    cx, cy = cell
    board[y + cy][x + cx] = p
