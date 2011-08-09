package nu.tengstrand.tetrisanalyzer.piecemove

import nu.tengstrand.tetrisanalyzer.piece._
import nu.tengstrand.tetrisanalyzer.settings.GameSettings
import nu.tengstrand.tetrisanalyzer.board.Board

/**
 * Is a holder for all valid moves on an empty board for every piece type.
 */
class AllValidPieceMovesForEmptyBoard(board: Board, settings: GameSettings, isSlidingEnabled: Boolean = false) {
  val startPieces = List(
    new ValidPieceMovesForEmptyBoard(board, new PieceI, settings, isSlidingEnabled).startMove,
    new ValidPieceMovesForEmptyBoard(board, new PieceZ, settings, isSlidingEnabled).startMove,
    new ValidPieceMovesForEmptyBoard(board, new PieceS, settings, isSlidingEnabled).startMove,
    new ValidPieceMovesForEmptyBoard(board, new PieceJ, settings, isSlidingEnabled).startMove,
    new ValidPieceMovesForEmptyBoard(board, new PieceL, settings, isSlidingEnabled).startMove,
    new ValidPieceMovesForEmptyBoard(board, new PieceT, settings, isSlidingEnabled).startMove,
    new ValidPieceMovesForEmptyBoard(board, new PieceO, settings, isSlidingEnabled).startMove
  )

  def startMoveForPiece(piece: Piece): PieceMove = startPieces(piece.number - 1)
}