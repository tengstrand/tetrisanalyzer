package nu.tengstrand.tetrisanalyzer.settings

import nu.tengstrand.tetrisanalyzer.piece.Piece
import nu.tengstrand.tetrisanalyzer.move.Move
import rotation.RotationSettings

/**
 * This interface defines the API of the game settings used by the Tetris engine.
 */
trait GameSettings extends RotationSettings {
  def pieceGeneratorSeed: Long
  def pieceStartMove(boardWidth: Int, piece: Piece): Move
  def firstGuaranteedFreeRowUnderStartPiece: Int
}