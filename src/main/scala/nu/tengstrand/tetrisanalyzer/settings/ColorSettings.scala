package nu.tengstrand.tetrisanalyzer.settings

import java.awt.Color

trait ColorSettings {
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