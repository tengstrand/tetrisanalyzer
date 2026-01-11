from tetrisanalyzer.board.core import empty_board


def is_incomplete(row):
    return 0 in row


def clear_rows(board):
    width = len(board[0])
    height = len(board)
    remaining_rows = [row for row in board if is_incomplete(row)]
    num_cleared_rows = height - len(remaining_rows)
    empty_rows = empty_board(width, num_cleared_rows)
    return empty_rows + remaining_rows
