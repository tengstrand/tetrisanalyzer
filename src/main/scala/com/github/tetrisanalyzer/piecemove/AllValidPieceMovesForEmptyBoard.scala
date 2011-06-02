package com.github.tetrisanalyzer.piecemove

import com.github.tetrisanalyzer.piece._
import com.github.tetrisanalyzer.settings.GameSettings
import com.github.tetrisanalyzer.board.Board

/**
 * Is a holder for all valid moves on an empty board for every piece type.
 */
class AllValidPieceMovesForEmptyBoard(board: Board, settings: GameSettings) {
  val startPieces = List(
    new ValidPieceMovesForEmptyBoard(board, new PieceI, settings).startMove,
    new ValidPieceMovesForEmptyBoard(board, new PieceZ, settings).startMove,
    new ValidPieceMovesForEmptyBoard(board, new PieceS, settings).startMove,
    new ValidPieceMovesForEmptyBoard(board, new PieceJ, settings).startMove,
    new ValidPieceMovesForEmptyBoard(board, new PieceL, settings).startMove,
    new ValidPieceMovesForEmptyBoard(board, new PieceT, settings).startMove,
    new ValidPieceMovesForEmptyBoard(board, new PieceO, settings).startMove
  )

  def startMoveForPiece(piece: Piece): PieceMove = startPieces(piece.number - 1)
}