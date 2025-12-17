from tetrisanalyzer.board import core

__all__ = ["core", "board"]

# Export functions from core as board module for convenience
from tetrisanalyzer.board.core import empty_board, set_cell, set_piece

board = core
