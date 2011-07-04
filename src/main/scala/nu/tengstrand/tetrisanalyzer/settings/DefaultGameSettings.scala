package nu.tengstrand.tetrisanalyzer.settings

import nu.tengstrand.tetrisanalyzer.piece.Piece
import nu.tengstrand.tetrisanalyzer.move.rotation.AnticlockwiseRotation
import nu.tengstrand.tetrisanalyzer.move.Move

/**
 * The default settings used by the Tetris engine.
 */
class DefaultGameSettings extends GameSettings {
  def pieceGeneratorSeed = 0
  def isSlidingEnabled = false
  val rotationDirection = new AnticlockwiseRotation
  def pieceStartMove(boardWidth: Int, piece: Piece) = new Move(0, if (boardWidth <= 4) 0 else (boardWidth - 2) / 2, 0)
  def firstFreeRowUnderStartPiece: Int = 2
}
