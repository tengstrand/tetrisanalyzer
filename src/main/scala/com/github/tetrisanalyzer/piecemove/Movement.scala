package com.github.tetrisanalyzer.piecemove

import com.github.tetrisanalyzer.move.rotation.RotationDirection

/**
 * Used by the algorithm that calculates the tree of linked moves
 * with all valid moves om an empty board.
 */
class Movement(val pieceMove: PieceMove, val direction: Direction) {
  def this(pieceMove: PieceMove) = this(pieceMove, new Down)

  def linkTo(linkedFromMovement: Movement) {
    if (direction == Direction.Down)
      linkedFromMovement.pieceMove.down = pieceMove
    else
      linkedFromMovement.pieceMove.asideAndRotate += pieceMove
  }

  def rotate(rotationType: RotationDirection, rotationModulus: Int, visitedPieceMoves: VisitedPieceMoves) = new Movement(visitedPieceMoves.get(pieceMove.move rotate(rotationType, rotationModulus)), new Rotate)
  def left(visitedPieceMoves: VisitedPieceMoves) = new Movement(visitedPieceMoves.get(pieceMove.move.left), new Left)
  def right(visitedPieceMoves: VisitedPieceMoves) = new Movement(visitedPieceMoves.get(pieceMove.move right), new Right)
  def down(visitedPieceMoves: VisitedPieceMoves) = new Movement(visitedPieceMoves.get(pieceMove.move down), new Down)

  override def toString = pieceMove + ", " + direction
}