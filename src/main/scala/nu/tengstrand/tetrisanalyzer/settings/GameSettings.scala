package nu.tengstrand.tetrisanalyzer.settings

import nu.tengstrand.tetrisanalyzer.piece.Piece
import nu.tengstrand.tetrisanalyzer.move.rotation.RotationDirection
import nu.tengstrand.tetrisanalyzer.move.Move
/**
 * This interface defines the API of the game settings used by the Tetris engine.
 */
trait GameSettings {
  def pieceGeneratorSeed: Long
  def rotationDirection: RotationDirection
  def pieceStartMove(boardWidth: Int, piece: Piece): Move
  def firstGuaranteedFreeRowUnderStartPiece: Int
}