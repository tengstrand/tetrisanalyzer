package com.github.tetrisanalyzer.settings

import com.github.tetrisanalyzer.piece.{Piece}
import com.github.tetrisanalyzer.move.rotation.AnticlockwiseRotation
import com.github.tetrisanalyzer.move.Move
import java.awt.Color

object DefaultGameSettings {
  private val Colors = Array(
    new Color(200, 200, 230), // 0 = Empty
    new Color(255,0,0),       // 1 = I, Red
    new Color(255, 151, 0),   // 2 = Z, Orange
    new Color(0, 184, 151),   // 3 = S, Cyan
    new Color(222, 184, 0),   // 4 = J, Yellow
    new Color(222, 0, 222),   // 5 = L, Purple
    new Color(71, 184, 0),    // 6 = T, Green
    new Color(0, 71, 222),    // 7 = O, Blue
    new Color(100, 100, 130), // 8 = Any Piece
    new Color(200, 200, 200)  // 9 = Wall
  )

  private val LineColors = Array.tabulate(Colors.size) {
      ((i) => (new Color(
        Colors(i).getRed / 2,
        Colors(i).getGreen / 2,
        Colors(i).getBlue / 2)))
  }

  Colors(0) = new Color(255, 255, 255);
}

/**
 * The default settings used by the Tetris engine.
 */
class DefaultGameSettings extends GameSettings {
  def isSlidingEnabled = false
  val rotationDirection = new AnticlockwiseRotation
  def pieceStartMove(boardWidth: Int, piece: Piece) = new Move(0, if (boardWidth <= 4) 0 else (boardWidth - 2) / 2, 0)

  def squareColor(element: Int) = DefaultGameSettings.Colors(element)
  def lineColor(element: Int) = DefaultGameSettings.LineColors(element)
}
