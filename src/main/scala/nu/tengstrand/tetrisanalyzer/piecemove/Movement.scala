package nu.tengstrand.tetrisanalyzer.piecemove

import nu.tengstrand.tetrisanalyzer.move.rotation.RotationDirection
import nu.tengstrand.tetrisanalyzer.settings.rotation.MoveAdjustment

/**
 * Used by the algorithm that calculates the linked list of moves
 * representing all valid moves om an empty board.
 */
class Movement(val pieceMove: PieceMove, val direction: Direction) {
  def this(pieceMove: PieceMove) = this(pieceMove, new Down)

  def linkTo(linkedFromMovement: Movement) {
    if (direction == Direction.Down)
      linkedFromMovement.pieceMove.down = pieceMove
    else
      linkedFromMovement.pieceMove.asideAndRotate += pieceMove
  }

  def rotate(adjust: MoveAdjustment, rotationType: RotationDirection, rotationModulus: Int, visitedPieceMoves: VisitedPieceMoves) = {
    new Movement(visitedPieceMoves.get(pieceMove.move rotate(adjust, rotationType, rotationModulus)), new Rotate)
  }
  def left(visitedPieceMoves: VisitedPieceMoves) = new Movement(visitedPieceMoves.get(pieceMove.move.left), new Left)
  def right(visitedPieceMoves: VisitedPieceMoves) = new Movement(visitedPieceMoves.get(pieceMove.move right), new Right)
  def down(visitedPieceMoves: VisitedPieceMoves) = new Movement(visitedPieceMoves.get(pieceMove.move down), new Down)

  override def toString = pieceMove + ", " + direction
}