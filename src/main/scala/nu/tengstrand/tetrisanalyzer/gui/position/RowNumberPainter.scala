package nu.tengstrand.tetrisanalyzer.gui.position

import java.awt.{Color, Graphics2D}
import nu.tengstrand.tetrisanalyzer.game.{ColoredPosition, Wall}
import nu.tengstrand.tetrisanalyzer.gui.FontChooser

class RowNumberPainter {
  private val fontChooser = new FontChooser

  def paintNumbers(position: ColoredPosition, rows: Seq[Int], columns: Seq[Int], g: Graphics2D) {
    g.setColor(Color.BLACK)

    val squareWidth = (columns(Wall.Left) - columns(Wall.Left - 1))

    fontChooser.setFont(squareWidth, g)

    for (number <- 1 to position.height - 2) {
      val row = position.height - number - 2
      val squareHeight = rows(row + 1) - rows(row)
      val x = columns(Wall.Left - 1) + squareWidth / 2  + (if (number < 10) -1 else -4)
      val y = rows(row) + (squareHeight-1) / 2 + 5
      g.drawString(number.toString, x, y)
    }
  }

}