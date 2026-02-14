from collections import deque

from tetrisanalyzer import board as board_ifc
from tetrisanalyzer.piece import piece
from tetrisanalyzer.piece.bitmask import rotation_bitmask
from tetrisanalyzer.piece.move import move
from tetrisanalyzer.piece.move import visit

def _placements(board, x, y, p, rotation, bitmask, valid_moves, visited_moves, rotation_move_fn, shapes):
    next_moves = deque([[x, y, rotation]])
    valid_placements = []

    while next_moves:
        x, y, rotation = next_moves.popleft()

        if visit.is_visited(visited_moves, x, y, rotation):
            continue

        down_move, placement = move.down(board, x, y, p, rotation, bitmask, shapes)

        moves = [
            move.left(board, x, y, p, rotation, bitmask, shapes),
            move.right(board, x, y, p, rotation, bitmask, shapes),
            rotation_move_fn(board, x, y, p, rotation, bitmask, shapes),
            down_move,
        ]
        moves = [m for m in moves if m is not None]

        next_moves.extend(moves)

        if placement is not None:
            valid_placements.extend(placement)

        valid_moves.append([x, y, rotation])
        visit.visit(visited_moves, x, y, rotation)

    return valid_placements


def placements(board, p, start_x, kick, shapes):
    y = 0
    rotation = 0
    bitmask = rotation_bitmask(shapes, p)
    visited_moves = board_ifc.empty_board(board_ifc.width(board), board_ifc.height(board))
    rotation_move_fn = move.rotation_fn(kick)

    if not move.is_valid_move(board, start_x, y, p, rotation, shapes):
        return []

    return _placements(board, start_x, y, p, rotation, bitmask, [], visited_moves, rotation_move_fn, shapes)
