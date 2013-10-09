package nu.tengstrand.tetrisanalyzer.settings

import nu.tengstrand.tetrisanalyzer.piece.Piece
import nu.tengstrand.tetrisanalyzer.move.Move
import rotation.DefaultRotationSettings

/**
 * The default settings used by the Tetris engine.
 */
class DefaultGameSettings extends GameSettings with DefaultRotationSettings {
  def pieceGeneratorSeed = 0
  def pieceStartMove(boardWidth: Int, piece: Piece) = new Move(0, if (boardWidth <= 4) 0 else (boardWidth - 2) / 2, 0)
  def firstGuaranteedFreeRowUnderStartPiece: Int = 2
}
