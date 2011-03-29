package com.github.tetrisanalyzer.piecemove

import com.github.tetrisanalyzer.piece._
import com.github.tetrisanalyzer.settings.GameSettings
import com.github.tetrisanalyzer.board.Board
import collection.mutable.Map

/**
 * Is a holder for all valid moves on an empty board.
 */
class AllValidPieceMovesForEmptyBoard(board: Board, settings: GameSettings) {
  private val startPieces = Map(
    new PieceI -> new ValidPieceMovesForEmptyBoard(board, new PieceI, settings).startMove,
    new PieceZ -> new ValidPieceMovesForEmptyBoard(board, new PieceZ, settings).startMove,
    new PieceS -> new ValidPieceMovesForEmptyBoard(board, new PieceS, settings).startMove,
    new PieceJ -> new ValidPieceMovesForEmptyBoard(board, new PieceJ, settings).startMove,
    new PieceL -> new ValidPieceMovesForEmptyBoard(board, new PieceL, settings).startMove,
    new PieceT -> new ValidPieceMovesForEmptyBoard(board, new PieceT, settings).startMove,
    new PieceO -> new ValidPieceMovesForEmptyBoard(board, new PieceO, settings).startMove
  )

  def startMoveForPiece(piece: Piece): PieceMove = {
    startPieces.get(piece) match {
      case Some(pieceMove) => pieceMove
      case _ => throw new IllegalStateException("Could not find a start move for piece " + piece + " on board " + board)
    }
  }
}