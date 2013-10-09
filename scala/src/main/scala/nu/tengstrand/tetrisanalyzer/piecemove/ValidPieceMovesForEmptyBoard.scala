package nu.tengstrand.tetrisanalyzer.piecemove

import nu.tengstrand.tetrisanalyzer.settings.GameSettings
import nu.tengstrand.tetrisanalyzer.piece.Piece
import nu.tengstrand.tetrisanalyzer.board.Board

/**
 * Calculates all valid moves for a given piece on an empty board.
 * If sliding is on, rotations and lateral movements can only occur in the top row.
 */
class ValidPieceMovesForEmptyBoard(val board: Board, val piece: Piece, settings: GameSettings, isSlidingEnabled: Boolean = false) {
  private val boardWidth = board.width
  private val boardHeight = board.height
  private var visitedPieceMoves = new VisitedPieceMoves(board, piece)
  private val rotationDirection = settings.rotationDirection

  private def markAsVisited(fromMovement: Movement, movement: Movement) {
    visitedPieceMoves.visit(movement)
    movement.linkTo(fromMovement)
  }

  private def isPieceInsideBoard(movement: Movement): Boolean = {
    val move = movement.pieceMove.move
    (move.x >= 0 && move.x + piece.width(move.rotation) <= boardWidth &&
      move.y >= 0 && move.y + piece.height(move.rotation) <= boardHeight)
  }

  /**
   * Calculates all valid moves as a linked list of moves by returning
   * the starting piece move for the board.
   */
  def startMove: PieceMove = {
    val startMove = settings.pieceStartMove(boardWidth, piece)
    val startMovement = new Movement(PieceMove(board, piece, startMove))
    val fromMovement = new Movement(PieceMove(board, piece, startMove.up))

    calculateValidMoves(fromMovement, startMovement)

    if (fromMovement.pieceMove.down == null)
      throw new IllegalStateException("Illegal start position for piece")

    fromMovement.pieceMove.down
  }

  private def calculateValidMoves(fromMovement: Movement, movement: Movement) {
    while (visitedPieceMoves.isUnvisited(movement) && isPieceInsideBoard(movement)) {
      markAsVisited(fromMovement, movement)
      if (isSlidingEnabled || movement.pieceMove.move.y == 0) {
        val moveAdjustment = settings.rotationAdjustment(movement.pieceMove.piece, movement.pieceMove.move.rotation)
        calculateValidMoves(movement, movement.rotate(moveAdjustment, rotationDirection, piece.rotationModulus, visitedPieceMoves))
        calculateValidMoves(movement, movement.left(visitedPieceMoves))
        calculateValidMoves(movement, movement.right(visitedPieceMoves))
      }
      calculateValidMoves(movement, movement.down(visitedPieceMoves))
    }
  }
}