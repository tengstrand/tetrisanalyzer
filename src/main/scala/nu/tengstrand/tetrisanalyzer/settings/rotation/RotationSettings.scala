package nu.tengstrand.tetrisanalyzer.settings.rotation

import nu.tengstrand.tetrisanalyzer.piece.Piece
import nu.tengstrand.tetrisanalyzer.move.rotation.RotationDirection

trait RotationSettings {
  def rotationDirection: RotationDirection
  def adjustment(piece: Piece, fromRotation: Int): MoveAdjustment
}