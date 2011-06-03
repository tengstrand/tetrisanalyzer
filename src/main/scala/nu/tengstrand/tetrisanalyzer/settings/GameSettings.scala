package nu.tengstrand.tetrisanalyzer.settings

import nu.tengstrand.tetrisanalyzer.piece.Piece
import nu.tengstrand.tetrisanalyzer.move.rotation.RotationDirection
import nu.tengstrand.tetrisanalyzer.move.Move
import java.awt.Color

/**
 * This interface defines the API of the game settings used by the Tetris engine.
 */
trait GameSettings {
  def isSlidingEnabled: Boolean
  def rotationDirection: RotationDirection
  def pieceStartMove(boardWidth: Int, piece: Piece): Move
  def firstFreeRowUnderStartPiece: Int

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