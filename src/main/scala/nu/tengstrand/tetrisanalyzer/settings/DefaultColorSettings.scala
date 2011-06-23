package nu.tengstrand.tetrisanalyzer.settings

import java.awt.Color

object DefaultColorSettings {
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
        (Colors(i).getRed * 0.6).toInt,
        (Colors(i).getGreen * 0.6).toInt,
        (Colors(i).getBlue * 0.6).toInt)))
  }

  Colors(0) = new Color(255, 255, 255);
}

class DefaultColorSettings extends ColorSettings {
  def squareColor(element: Int) = DefaultColorSettings.Colors(element)
  def lineColor(element: Int) = DefaultColorSettings.LineColors(element)
}