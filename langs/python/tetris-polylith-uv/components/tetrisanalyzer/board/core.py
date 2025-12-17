def empty_board(width, height):
    return [[0] * width for _ in range(height)]

def set_cell(board, p, x, y, cell):
    cx, cy = cell
    board[y + cy][x + cx] = p

def set_piece(board, p, x, y, piece):
    for cell in piece:
        set_cell(board, p, x, y, cell)
    return board