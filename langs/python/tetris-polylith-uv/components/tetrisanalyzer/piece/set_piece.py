def set_piece(board, p, x, y, piece_cells):
    for cx, cy in piece_cells:
        board[y + cy][x + cx] = p
