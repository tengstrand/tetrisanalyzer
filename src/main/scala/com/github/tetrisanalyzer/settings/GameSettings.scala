package com.github.tetrisanalyzer.settings

import com.github.tetrisanalyzer.piece.Piece
import com.github.tetrisanalyzer.move.rotation.RotationDirection
import com.github.tetrisanalyzer.move.Move

/**
 * This interface defines the API of the game settings used by the Tetris engine.
 */
trait GameSettings {
  def isSlidingEnabled: Boolean
  def rotationDirection: RotationDirection
  def pieceStartMove(boardWidth: Int, piece: Piece): Move
}