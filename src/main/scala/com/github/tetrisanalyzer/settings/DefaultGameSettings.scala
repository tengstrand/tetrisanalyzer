package com.github.tetrisanalyzer.settings

import com.github.tetrisanalyzer.piece.{Piece}
import com.github.tetrisanalyzer.move.rotation.AnticlockwiseRotation
import com.github.tetrisanalyzer.move.Move

/**
 * The default settings used by the Tetris engine.
 */
class DefaultGameSettings extends GameSettings {
  def slidingEnabled = false
  val rotationDirection = new AnticlockwiseRotation
  def pieceStartMove(boardWidth: Int, piece: Piece) = new Move(0, if (boardWidth <= 4) 0 else (boardWidth - 2) / 2, 0)
}
