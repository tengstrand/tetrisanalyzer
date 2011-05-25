package com.github.tetrisanalyzer.settings

import com.github.tetrisanalyzer.piece.Piece
import com.github.tetrisanalyzer.move.rotation.RotationDirection
import com.github.tetrisanalyzer.move.Move
import java.awt.Color

/**
 * This interface defines the API of the game settings used by the Tetris engine.
 */
trait GameSettings {
  def isSlidingEnabled: Boolean
  def rotationDirection: RotationDirection
  def pieceStartMove(boardWidth: Int, piece: Piece): Move

  /**
   * Returns the colour of the square for the specified position
   * element which can be empty (0), a piece (1-8) or wall (9).
   */
  def squareColor(element: Int): Color

  /**
   * Returns the colour of the square line for the specified position
   * element which can be empty (0), a piece (1-8) or wall (9).
   */
  def lineColor(element: Int): Color

}